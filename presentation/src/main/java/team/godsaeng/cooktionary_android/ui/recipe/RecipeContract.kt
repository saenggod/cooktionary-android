package team.godsaeng.cooktionary_android.ui.recipe

import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.base.BaseContract
import javax.annotation.concurrent.Immutable

interface RecipeContract : BaseContract<RecipeContract.UiState, RecipeContract.UiEvent, RecipeContract.UiEffect> {
    @Immutable
    data class UiState(
        val isLoading: Boolean = false,
        val startIndex: Int = 0,
        val recipeList: RecipeList = RecipeList(emptyList())
    )

    sealed interface UiEvent {
        data class OnStarted(val index: Int) : UiEvent

        data class OnClickSave(
            val recipeId: Int,
            val isSaved: Boolean
        ) : UiEvent

        data object OnClickProfile : UiEvent
    }

    sealed interface UiEffect {
        data object GoToProfile : UiEffect
    }
}