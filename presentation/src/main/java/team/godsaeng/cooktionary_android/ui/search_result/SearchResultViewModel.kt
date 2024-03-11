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
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEffect.GoToRecipe
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnClickRecipe
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnRefreshed
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiState
import team.godsaeng.cooktionary_android.util.getExceptionHandler
import team.godsaeng.domain.model.use_case.recipe.GetRecipeListUseCase
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val getRecipeListUseCase: GetRecipeListUseCase
) : ViewModel(), SearchResultContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect: Channel<UiEffect> = Channel(capacity = Channel.BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: SearchResultContract.UiEvent) = when (event) {
        is OnStarted -> onStarted(event.ingredientNameList)

        is OnRefreshed -> onRefreshed()

        is OnClickRecipe -> onClickRecipe(event.index)
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
        viewModelScope.launch {
            _uiEffect.send(GoToRecipe(index))
        }
    }
}