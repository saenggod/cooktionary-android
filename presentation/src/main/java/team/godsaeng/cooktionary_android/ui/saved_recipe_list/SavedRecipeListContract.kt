package team.godsaeng.cooktionary_android.ui.saved_recipe_list

import androidx.compose.runtime.Immutable
import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.base.BaseContract

interface SavedRecipeListContract : BaseContract<SavedRecipeListContract.UiState, SavedRecipeListContract.UiEvent, SavedRecipeListContract.UiEffect> {
    @Immutable
    data class UiState(
        val isLoading: Boolean = false,
        val isPagerMode: Boolean = false,
        val recipeList: RecipeList = RecipeList(emptyList()),
        val startIndex: Int = 0,
    )

    sealed interface UiEvent {
        data object OnStarted : UiEvent

        data class OnClickRecipe(val index: Int) : UiEvent

        data object OnClickProfile : UiEvent

        data class OnClickSaveRecipe(
            val recipeId: Int,
            val isSaved: Boolean
        ) : UiEvent

        data object OnBackPressed : UiEvent
    }

    sealed interface UiEffect {
        data object GoToProfile : UiEffect
    }
}