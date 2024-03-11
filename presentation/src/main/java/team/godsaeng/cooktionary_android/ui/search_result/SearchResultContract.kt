package team.godsaeng.cooktionary_android.ui.search_result

import androidx.compose.runtime.Immutable
import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.base.BaseContract

interface SearchResultContract : BaseContract<SearchResultContract.UiState, SearchResultContract.UiEvent, SearchResultContract.UiEffect> {
    @Immutable
    data class UiState(
        val isLoading: Boolean = false,
        val pullFraction: Float = 0f,
        val isRefreshing: Boolean = false,
        val userIngredientNameList: List<String> = emptyList(),
        val recipeList: RecipeList = RecipeList(emptyList())
    )

    sealed interface UiEvent {
        data class OnStarted(val ingredientNameList: List<String>) : UiEvent

        data object OnRefreshed : UiEvent

        data class OnClickRecipe(val index: Int) : UiEvent

        data object OnClickProfile : UiEvent
    }

    sealed interface UiEffect {
        data class GoToRecipe(val index: Int) : UiEffect

        data object GoToProfile : UiEffect
    }
}