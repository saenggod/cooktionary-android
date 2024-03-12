package team.godsaeng.cooktionary_android.ui.search_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEffect
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEffect.GoToProfile
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnBackPressed
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnClickProfile
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnClickRecipe
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnClickSaveRecipe
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnRefreshed
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiState
import team.godsaeng.cooktionary_android.util.getExceptionHandler
import team.godsaeng.domain.model.use_case.recipe.DeleteSavedRecipeUseCase
import team.godsaeng.domain.model.use_case.recipe.GetRecipeListUseCase
import team.godsaeng.domain.model.use_case.recipe.SaveRecipeUseCase
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val getRecipeListUseCase: GetRecipeListUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase
) : ViewModel(), SearchResultContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect: Channel<UiEffect> = Channel(capacity = Channel.BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: SearchResultContract.UiEvent) = when (event) {
        is OnStarted -> onStarted(event.ingredientNameList)

        is OnRefreshed -> onRefreshed()

        is OnClickRecipe -> onClickRecipe(event.index)

        is OnClickProfile -> onClickProfile()

        is OnClickSaveRecipe -> onClickSaveRecipe(
            recipeId = event.recipeId,
            isSaved = event.isSaved
        )

        is OnBackPressed -> onBackPressed()
    }

    private val exceptionHandler = getExceptionHandler(
        onUnknownHostException = {}
    )

    private fun onStarted(ingredientNameList: List<String>) {
        _uiState.update {
            it.copy(userIngredientNameList = ingredientNameList)
        }

        search(ingredientNameList)
    }

    private fun search(ingredientNameList: List<String>) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            getRecipeListUseCase(ingredientNameList).handle(
                onSuccess = { recipeList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recipeList = RecipeList(
                                recipeList.map { recipe ->
                                    val recipeIngredientNames = recipe.ingredientNameList
                                    var existingIngredientCount = 0

                                    for (userIngredient in ingredientNameList) {
                                        for (recipeIngredient in recipeIngredientNames) {
                                            if (userIngredient == recipeIngredient) {
                                                existingIngredientCount++
                                            }
                                        }
                                    }

                                    recipe.copy(neededIngredientCount = recipeIngredientNames.size - existingIngredientCount)
                                }
                            )
                        )
                    }
                },
                onFailure = {

                }
            )
        }
    }

    private fun onRefreshed() {
        search(uiState.value.userIngredientNameList)
    }

    private fun onClickRecipe(index: Int) {
        _uiState.update {
            it.copy(
                isPagerMode = true,
                startIndex = index
            )
        }
    }

    private fun onClickProfile() {
        viewModelScope.launch {
            _uiEffect.send(GoToProfile)
        }
    }

    private fun onClickSaveRecipe(
        recipeId: Int,
        isSaved: Boolean
    ) {
        viewModelScope.launch {
            if (!isSaved) {
                saveRecipeUseCase(recipeId).handle(
                    onSuccess = {
                        _uiState.update {
                            it.copy(recipeList = RecipeList(it.recipeList.values.map { if (it.id == recipeId) it.copy(isSaved = true) else it }))
                        }
                    },
                    onFailure = {

                    }
                )
            } else {
                deleteSavedRecipeUseCase(recipeId).handle(
                    onSuccess = {
                        _uiState.update {
                            it.copy(recipeList = RecipeList(it.recipeList.values.map { if (it.id == recipeId) it.copy(isSaved = false) else it }))
                        }
                    },
                    onFailure = {

                    }
                )
            }
        }
    }

    private fun onBackPressed() {
        if (uiState.value.isPagerMode) {
            _uiState.update {
                it.copy(isPagerMode = false)
            }
        }
    }
}