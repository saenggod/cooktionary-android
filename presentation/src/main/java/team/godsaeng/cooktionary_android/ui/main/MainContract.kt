package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.ui.geometry.Offset
import org.burnoutcrew.reorderable.ItemPosition
import team.godsaeng.cooktionary_android.ui.base.BaseContract

sealed interface MainContract : BaseContract<MainContract.UiState, MainContract.UiEvent, MainContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,
        val ingredientDisplayList: List<String> = listOf("1", "2", "3"),
        val ingredientButtonList: List<String> = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "1", "2", "3", "4", "5", "6", "7", "8"),
        val isDragging: Boolean = false,
        val isDeletable: Boolean = false,
        val draggingPosition: Offset = Offset(0f, 0f),
        val trashCanPosition: Offset = Offset(0f, 0f),
        val trashCanSize: Int = 0,
    )

    sealed interface UiEvent {
        data class OnDrag(val offset: Offset) : UiEvent

        data class OnDragEnd(val deletableItemIndex: Int) : UiEvent

        data class OnOrderChanged(
            val from: ItemPosition,
            val to: ItemPosition
        ) : UiEvent

        data class OnTrashCanMeasured(
            val trashCanSize: Int,
            val trashCanPosition: Offset
        ) : UiEvent
    }

    sealed interface UiEffect {

    }
}