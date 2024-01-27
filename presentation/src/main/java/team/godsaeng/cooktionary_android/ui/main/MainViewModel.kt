package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ItemPosition
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect.ClearFocus
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonDragged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonDraggingEnded
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonOrderChanged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickAddDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickRemoveDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDone
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTyped
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), MainContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: MainContract.UiEvent) = when (event) {
        is OnClickAddDisplay -> onClickAddDisplay()

        is OnTyped -> onTyped(event.text)

        is OnClickDisplay -> onClickDisplay(
            index = event.index,
            ingredient = event.ingredient
        )

        is OnDone -> onDone(event.index)

        is OnClickRemoveDisplay -> onClickRemoveDisplay(event.index)

        is OnButtonDragged -> onDragged(event.offset)

        is OnButtonDraggingEnded -> onDraggingEnded(event.removableItemIndex)

        is OnButtonOrderChanged -> onOrderChanged(
            from = event.from,
            to = event.to
        )

        is OnTrashCanMeasured -> onTrashCanMeasured(
            trashCanSize = event.trashCanSize,
            trashCanPosition = event.trashCanPosition
        )
    }

    private fun onClickAddDisplay() {
        val displayList = uiState.value.displayList
        if (displayList.isNotEmpty() && displayList.last() == null) {

        } else {
            val newList = displayList.toMutableList().apply { add(null) }
            val targetIndex = newList.lastIndex * 2

            _uiState.update {
                it.copy(
                    displayList = newList,
                    selectedDisplayIndex = targetIndex,
                    typedText = ""
                )
            }
        }
    }

    private fun onTyped(text: String) {
        _uiState.update {
            it.copy(typedText = text)
        }
    }

    private fun onClickDisplay(
        index: Int,
        ingredient: String?
    ) {
        _uiState.update {
            it.copy(
                selectedDisplayIndex = index,
                typedText = ingredient.orEmpty()
            )
        }
    }

    private fun onDone(editedIndex: Int) {
        _uiState.update {
            it.copy(
                displayList = it.displayList.mapIndexed { index, s ->
                    if (editedIndex == index) {
                        it.typedText
                    } else {
                        s
                    }
                },
                typedText = "",
                selectedDisplayIndex = -1
            )
        }

        viewModelScope.launch {
            _uiEffect.emit(ClearFocus)
        }
    }

    private fun onClickRemoveDisplay(index: Int) {
        _uiState.update {
            it.copy(
                displayList = it.displayList.toMutableList().apply { removeAt(index) },
                selectedDisplayIndex = -1
            )
        }

        viewModelScope.launch {
            _uiEffect.emit(ClearFocus)
        }
    }

    private fun onDragged(offset: Offset) {
        _uiState.update {
            it.copy(
                buttonDraggingPosition = offset,
                isButtonDragging = true
            )
        }

        val draggingXPosition = uiState.value.buttonDraggingPosition.x
        val draggingYPosition = uiState.value.buttonDraggingPosition.y
        val trashCanPosition = uiState.value.trashCanPosition
        val trashCanSize = uiState.value.trashCanSize
        val trashCanXRange = trashCanPosition.x..trashCanPosition.x + trashCanSize
        val trashCanYRange = trashCanPosition.y..trashCanPosition.y + trashCanSize

        _uiState.update {
            it.copy(isButtonRemovable = draggingXPosition in trashCanXRange && draggingYPosition in trashCanYRange)
        }
    }

    private fun onDraggingEnded(deletableItemIndex: Int) {
        if (uiState.value.isButtonRemovable) {
            _uiState.update {
                it.copy(
                    buttonList = it.buttonList.toMutableList().apply {
                        removeAt(deletableItemIndex)
                    }
                )
            }
        }

        _uiState.update {
            it.copy(
                buttonDraggingPosition = Offset.Zero,
                isButtonDragging = false,
                isButtonRemovable = false
            )
        }
    }

    private fun onOrderChanged(
        from: ItemPosition,
        to: ItemPosition
    ) {
        _uiState.update {
            it.copy(buttonList = it.buttonList.toMutableList().apply { add(to.index, removeAt(from.index)) })
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
}