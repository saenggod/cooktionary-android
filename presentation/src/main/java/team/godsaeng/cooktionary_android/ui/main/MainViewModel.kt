package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.burnoutcrew.reorderable.ItemPosition
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickAddIngredientButton
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDragged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDraggingEnded
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnIngredientTyped
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnOrderChanged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTypingIngredientEnded
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), MainContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>()
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: MainContract.UiEvent) = when (event) {
        is OnIngredientTyped -> onIngredientTyped(event.typedString)

        is OnTypingIngredientEnded -> onTypingIngredientEnded()

        is OnDragged -> onDragged(event.offset)

        is OnDraggingEnded -> onDraggingEnded(event.deletableItemIndex)

        is OnOrderChanged -> onOrderChanged(
            from = event.from,
            to = event.to
        )

        is OnTrashCanMeasured -> onTrashCanMeasured(
            event.trashCanSize,
            event.trashCanPosition
        )

        is OnClickAddIngredientButton -> onClickAddIngredientButton()
    }

    private fun onIngredientTyped(typedString: String) {
        _uiState.update {
            it.copy(typedIngredient = typedString)
        }
    }

    private fun onTypingIngredientEnded() {
        // todo : search Ingredient
    }

    private fun onDragged(offset: Offset) {
        _uiState.update {
            it.copy(
                draggingPosition = offset,
                isDragging = true
            )
        }

        val draggingXPosition = uiState.value.draggingPosition.x
        val draggingYPosition = uiState.value.draggingPosition.y
        val trashCanPosition = uiState.value.trashCanPosition
        val trashCanSize = uiState.value.trashCanSize
        val trashCanXRange = trashCanPosition.x..trashCanPosition.x + trashCanSize
        val trashCanYRange = trashCanPosition.y..trashCanPosition.y + trashCanSize

        _uiState.update {
            it.copy(isDeletable = draggingXPosition in trashCanXRange && draggingYPosition in trashCanYRange)
        }
    }

    private fun onDraggingEnded(deletableItemIndex: Int) {
        if (uiState.value.isDeletable) {
            _uiState.update {
                it.copy(
                    ingredientButtonList = it.ingredientButtonList.toMutableList().apply {
                        removeAt(deletableItemIndex)
                    }
                )
            }
        }

        _uiState.update {
            it.copy(
                draggingPosition = Offset.Zero,
                isDragging = false,
                isDeletable = false
            )
        }
    }

    private fun onOrderChanged(
        from: ItemPosition,
        to: ItemPosition
    ) {
        _uiState.update {
            it.copy(ingredientButtonList = it.ingredientButtonList.toMutableList().apply { add(to.index, removeAt(from.index)) })
        }
    }

    private fun onTrashCanMeasured(
        trashCanSize: Int,
        trashCanPosition: Offset
    ) {
        _uiState.update {
            it.copy(
                trashCanSize = trashCanSize,
                trashCanPosition = trashCanPosition
            )
        }
    }

    private fun onClickAddIngredientButton() {

    }
}