package team.godsaeng.cooktionary_android.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDrag
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDragStart
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDragStop
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnIngredientButtonMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), MainContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: MainContract.UiEvent) = when (event) {
        is OnDragStart -> onDragStart(
            ingredient = event.ingredient,
            startingXPosition = event.startingXPosition,
            startingYPosition = event.startingYPosition
        )

        is OnDragStop -> onDragStop()

        is OnDrag -> onDrag(
            event.draggedXOffset,
            event.draggedYOffset
        )

        is OnIngredientButtonMeasured -> onIngredientButtonMeasured(event.size)

        is OnTrashCanMeasured -> onTrashCanMeasured(
            xPosition = event.xPosition,
            yPosition = event.yPosition
        )
    }

    private fun onDragStart(
        ingredient: String,
        startingXPosition: Float,
        startingYPosition: Float
    ) {
        _uiState.update {
            it.copy(
                isDragging = true,
                draggingIngredient = ingredient,
                draggedXPosition = startingXPosition - (uiState.value.ingredientButtonSize / 2),
                draggedYPosition = startingYPosition - (uiState.value.ingredientButtonSize / 2),
            )
        }
    }

    private fun onDragStop() {
        _uiState.update {
            it.copy(
                isDragging = false,
                isDeletable = false,
                draggingIngredient = null,
                draggedXPosition = 0f,
                draggedYPosition = 0f
            )
        }
    }

    private fun onDrag(
        draggedXOffset: Float,
        draggedYOffset: Float
    ) {
        _uiState.update {
            it.copy(
                draggedXPosition = uiState.value.draggedXPosition + draggedXOffset,
                draggedYPosition = uiState.value.draggedYPosition + draggedYOffset
            )
        }

        val trashCanSize = uiState.value.trashCanSize
        val validXRange = uiState.value.trashCanXPosition - trashCanSize..uiState.value.trashCanXPosition + trashCanSize
        val validYRange = uiState.value.trashCanYPosition - trashCanSize..uiState.value.trashCanYPosition + trashCanSize

        if (uiState.value.draggedXPosition in validXRange && uiState.value.draggedYPosition in validYRange) {
            _uiState.update {
                it.copy(isDeletable = true)
            }
        } else {
            if (uiState.value.isDeletable) {
                _uiState.update {
                    it.copy(isDeletable = false)
                }
            }
        }
    }

    private fun onIngredientButtonMeasured(size: Int) {
        _uiState.update {
            it.copy(
                ingredientButtonSize = size,
                trashCanSize = size
            )
        }
    }

    private fun onTrashCanMeasured(
        xPosition: Float,
        yPosition: Float
    ) {
        _uiState.update {
            it.copy(
                trashCanXPosition = xPosition,
                trashCanYPosition = yPosition
            )
        }
    }
}