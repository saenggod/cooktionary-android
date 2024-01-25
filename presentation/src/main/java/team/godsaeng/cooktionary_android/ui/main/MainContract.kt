package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.ui.geometry.Offset
import org.burnoutcrew.reorderable.ItemPosition
import team.godsaeng.cooktionary_android.ui.base.BaseContract

sealed interface MainContract : BaseContract<MainContract.UiState, MainContract.UiEvent, MainContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val addedIngredientCount: Int = 1,
        val typedIngredient: String = "",
        val ingredientDisplayList: List<String?> = listOf(null),
        val ingredientButtonList: List<String> = emptyList(),
        val isDragging: Boolean = false,
        val isRemovable: Boolean = false,
        val draggingPosition: Offset = Offset(0f, 0f),
        val trashCanPosition: Offset = Offset(0f, 0f),
        val trashCanSize: Int = 0,
    )

    sealed interface UiEvent {
        data class OnIngredientTyped(val typedString: String) : UiEvent

        data class OnIngredientDisplayFocused(
            val ingredient: String?,
            val index: Int
        ) : UiEvent

        data class OnTypingIngredientEnded(val index: Int) : UiEvent

        data class OnClickRemoveIngredientDisplay(val index: Int) : UiEvent

        data class OnClickAddIngredientDisplay(val ingredient: String?) : UiEvent

        data class OnDragged(val offset: Offset) : UiEvent

        data class OnDraggingEnded(val removableItemIndex: Int) : UiEvent

        data class OnOrderChanged(
            val from: ItemPosition,
            val to: ItemPosition
        ) : UiEvent

        data class OnTrashCanMeasured(
            val trashCanSize: Int,
            val trashCanPosition: Offset
        ) : UiEvent

        data object OnClickAddIngredientButton : UiEvent

        data object OnClearingFocusNeeded : UiEvent

        data object OnClickReset : UiEvent
    }

    sealed interface UiEffect {
        data class ScrollToEndOfDisplayList(val targetIndex: Int) : UiEffect

        data class ScrollToClickedDisplay(val targetIndex: Int) : UiEffect

        data object ClearFocus : UiEffect

        data object RequestFocus : UiEffect
    }
}