package team.godsaeng.cooktionary_android.ui.recipe

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEffect
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiState
import team.godsaeng.domain.model.use_case.recipe.GetLoadedRecipeListUseCase
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val getLoadedRecipeListUseCase: GetLoadedRecipeListUseCase
) : ViewModel(), RecipeContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<UiEffect>(capacity = Channel.BUFFERED)
    override val uiEffect: Flow<UiEffect> = _uiEffect.receiveAsFlow()

    override fun uiEvent(event: RecipeContract.UiEvent) = when (event) {
        is OnStarted -> onStarted(event.index)
    }

    private fun onStarted(index: Int) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        val recipeList = getLoadedRecipeListUseCase()

        _uiState.update {
            it.copy(
                isLoading = false,
                startIndex = index,
                recipeList = RecipeList(recipeList)
            )
        }
    }
}