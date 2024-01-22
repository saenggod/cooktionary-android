package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.ui.geometry.Offset
import org.burnoutcrew.reorderable.ItemPosition
import team.godsaeng.cooktionary_android.ui.base.BaseContract

sealed interface MainContract : BaseContract<MainContract.UiState, MainContract.UiEvent, MainContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val addedIngredientCount: Int = 0,
        val typedIngredient: String = "",
        val ingredientDisplayList: List<String?> = listOf(null),
        val ingredientButtonList: List<String> = emptyList(),
        val isDragging: Boolean = false,
        val isDeletable: Boolean = false,
        val draggingPosition: Offset = Offset(0f, 0f),
        val trashCanPosition: Offset = Offset(0f, 0f),
        val trashCanSize: Int = 0,
    )

    sealed interface UiEvent {
        data class OnIngredientTyped(val typedString: String) : UiEvent

        data object OnTypingIngredientEnded : UiEvent

        data class OnDragged(val offset: Offset) : UiEvent

        data class OnDraggingEnded(val deletableItemIndex: Int) : UiEvent

        data class OnOrderChanged(
            val from: ItemPosition,
            val to: ItemPosition
        ) : UiEvent

        data class OnTrashCanMeasured(
            val trashCanSize: Int,
            val trashCanPosition: Offset
        ) : UiEvent

        data object OnClickAddIngredientButton : UiEvent
    }

    sealed interface UiEffect {

    }
}