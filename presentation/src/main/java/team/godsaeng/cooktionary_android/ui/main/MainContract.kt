package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.ui.geometry.Offset
import org.burnoutcrew.reorderable.ItemPosition
import team.godsaeng.cooktionary_android.ui.base.BaseContract
import team.godsaeng.domain.model.model.ingredient.Ingredient

sealed interface MainContract : BaseContract<MainContract.UiState, MainContract.UiEvent, MainContract.UiEffect> {
    data class UiState(
        val displayList: List<Ingredient?> = emptyList(),
        val buttonList: List<Ingredient> = emptyList(),
        val selectedDisplayIndex: Int = -1,
        val typedText: String = "",
        val isButtonDragging: Boolean = false,
        val isButtonRemovable: Boolean = false,
        val buttonDraggingPosition: Offset = Offset(0f, 0f),
        val trashCanPosition: Offset = Offset(0f, 0f),
        val trashCanSize: Int = 0,
    )

    sealed interface UiEvent {
        data object OnStarted : UiEvent

        data object OnClickAddDisplay : UiEvent

        data class OnTyped(val text: String) : UiEvent

        data class OnClickDisplay(
            val index: Int,
            val ingredient: Ingredient?
        ) : UiEvent

        data class OnDone(val index: Int) : UiEvent

        data class OnClickRemoveDisplay(val index: Int) : UiEvent

        data class OnButtonDragged(val offset: Offset) : UiEvent

        data class OnButtonDraggingEnded(val removableItemIndex: Int) : UiEvent

        data class OnButtonOrderChanged(
            val from: ItemPosition,
            val to: ItemPosition
        ) : UiEvent

        data class OnTrashCanMeasured(
            val trashCanSize: Int,
            val trashCanPosition: Offset
        ) : UiEvent

        data object OnClickReset : UiEvent
    }

    sealed interface UiEffect {
        data object ClearFocus : UiEffect
    }
}