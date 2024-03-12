package team.godsaeng.cooktionary_android.ui.recipe

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
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEffect
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEffect.GoToProfile
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent.OnClickProfile
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent.OnClickSave
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiState
import team.godsaeng.domain.model.use_case.recipe.DeleteSavedRecipeUseCase
import team.godsaeng.domain.model.use_case.recipe.SaveRecipeUseCase
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val deleteSavedRecipeUseCase: DeleteSavedRecipeUseCase
) : ViewModel(), RecipeContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>(capacity = Channel.BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: RecipeContract.UiEvent) = when (event) {
        is OnStarted -> onStarted(event.index)

        is OnClickSave -> onClickSave(
            recipeId = event.recipeId,
            isSaved = event.isSaved
        )

        is OnClickProfile -> onClickProfile()
    }

    private fun onStarted(index: Int) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                startIndex = index,
            )
        }
    }

    private fun onClickSave(
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

    private fun onClickProfile() {
        viewModelScope.launch {
            _uiEffect.send(GoToProfile)
        }
    }
}