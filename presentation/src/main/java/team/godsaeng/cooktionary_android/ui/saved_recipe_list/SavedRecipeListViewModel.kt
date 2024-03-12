package team.godsaeng.cooktionary_android.ui.saved_recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEffect
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEffect.GoToProfile
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnBackPressed
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnClickProfile
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnClickRecipe
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnClickSaveRecipe
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiState
import team.godsaeng.domain.model.use_case.recipe.DeleteSavedRecipeUseCase
import team.godsaeng.domain.model.use_case.recipe.GetSavedRecipeListUseCase
import team.godsaeng.domain.model.use_case.recipe.SaveRecipeUseCase
import javax.inject.Inject

@HiltViewModel
class SavedRecipeListViewModel @Inject constructor(
    private val getSavedRecipeListUseCase: GetSavedRecipeListUseCase,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase
) : ViewModel(), SavedRecipeListContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>(capacity = BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: UiEvent) = when (event) {
        is OnStarted -> onStarted()

        is OnClickRecipe -> onClickRecipe(event.index)

        is OnClickProfile -> onClickProfile()

        is OnClickSaveRecipe -> onClickSaveRecipe(
            recipeId = event.recipeId,
            isSaved = event.isSaved
        )

        is OnBackPressed -> onBackPressed()
    }

    private fun onStarted() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            getSavedRecipeListUseCase().handle(
                onSuccess = { recipeList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recipeList = RecipeList(recipeList)
                        )
                    }
                },
                onFailure = {

                }
            )
        }
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