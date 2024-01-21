package team.godsaeng.cooktionary_android.ui.main

import team.godsaeng.cooktionary_android.ui.base.BaseContract

sealed interface MainContract : BaseContract<MainContract.UiState, MainContract.UiEvent, MainContract.UiEffect> {
    data class UiState(
        val isLoading: Boolean = false,

        val ingredientDisplayList: List<String> = listOf("1", "2", "3"),

        val ingredientButtonList: List<String> = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i"),

        val isDragging: Boolean = false,
        val isDeletable: Boolean = false,
        val draggingIngredient: String? = null,
        val draggedXPosition: Float = 0f,
        val draggedYPosition: Float = 0f,
        val ingredientButtonSize: Int = 0,
        val trashCanXPosition: Float = 0f,
        val trashCanYPosition: Float = 0f,
        val trashCanSize: Int = 0,
    )

    sealed interface UiEvent {
        data class OnDragStart(
            val ingredient: String,
            val startingXPosition: Float,
            val startingYPosition: Float
        ) : UiEvent

        data object OnDragStop : UiEvent

        data class OnDrag(
            val draggedXOffset: Float,
            val draggedYOffset: Float
        ) : UiEvent

        data class OnIngredientButtonMeasured(val size: Int) : UiEvent

        data class OnTrashCanMeasured(
            val xPosition: Float,
            val yPosition: Float
        ) : UiEvent
    }

    sealed interface UiEffect {

    }
}