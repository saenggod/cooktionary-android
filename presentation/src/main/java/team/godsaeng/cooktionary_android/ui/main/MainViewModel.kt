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
import team.godsaeng.cooktionary_android.model.wrapper.ingredient.NotNullIngredientList
import team.godsaeng.cooktionary_android.model.wrapper.ingredient.NullableIngredientList
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect.ClearFocus
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonDragged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonDraggingEnded
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonOrderChanged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickAddDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickRemoveDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickReset
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDone
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTyped
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiState
import team.godsaeng.cooktionary_android.util.getExceptionHandler
import team.godsaeng.domain.model.model.ingredient.Ingredient
import team.godsaeng.domain.model.use_case.ingredient.GetIngredientUseCase
import team.godsaeng.domain.model.use_case.ingredient.GetMyIngredientListUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getIngredientUseCase: GetIngredientUseCase,
    private val getMyIngredientListUseCase: GetMyIngredientListUseCase
) : ViewModel(), MainContract {
    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val uiEffect: SharedFlow<UiEffect> = _uiEffect.asSharedFlow()

    override fun uiEvent(event: MainContract.UiEvent) = when (event) {
        is OnStarted -> onStarted()

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

        is OnClickReset -> onClickReset()
    }

    private val exceptionHandler = getExceptionHandler(
        onUnknownHostException = {}
    )

    private fun onStarted() {
        viewModelScope.launch {
            getMyIngredientListUseCase().handle(
                onSuccess = { ingredientList ->
                    _uiState.update {
                        it.copy(buttonList = NotNullIngredientList(ingredientList))
                    }
                },
                onFailure = {
                    // todo: error handle
                }
            )
        }
    }

    private fun onClickAddDisplay() {
        val displayList = uiState.value.displayList
        if (displayList.values.isNotEmpty() && displayList.values.last() == null) {

        } else {
            val newList = displayList.values.toMutableList().apply { add(null) }
            val targetIndex = newList.lastIndex * 2

            _uiState.update {
                it.copy(
                    displayList = NullableIngredientList(newList),
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
        ingredient: Ingredient?
    ) {
        _uiState.update {
            it.copy(
                selectedDisplayIndex = index,
                typedText = ingredient?.name.orEmpty()
            )
        }
    }

    private fun onDone(editedIndex: Int) {
        val ingredientName = uiState.value.typedText

        viewModelScope.launch {
            _uiEffect.emit(ClearFocus)
        }

        viewModelScope.launch(exceptionHandler) {
            getIngredientUseCase(ingredientName).handle(
                onSuccess = { ingredient ->
                    addDisplayAndButton(
                        editedIndex = editedIndex,
                        ingredient = ingredient
                    )
                },
                onFailure = { error ->
                    when (error.code) {
                        // todo: handle error with code
                    }
                }
            )
        }
    }

    private fun addDisplayAndButton(
        editedIndex: Int,
        ingredient: Ingredient
    ) {
        _uiState.update {
            it.copy(
                typedText = "",
                selectedDisplayIndex = -1,
                displayList = NullableIngredientList(it.displayList.values.mapIndexed { index, original -> if (index == editedIndex) ingredient else original }),
                buttonList = NotNullIngredientList(it.buttonList.values.toMutableList().apply { add(0, ingredient) })
            )
        }
    }

    private fun onClickRemoveDisplay(index: Int) {
        _uiState.update {
            it.copy(
                displayList = NullableIngredientList(it.displayList.values.toMutableList().apply { removeAt(index) }),
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
                it.copy(buttonList = NotNullIngredientList(it.buttonList.values.toMutableList().apply { removeAt(deletableItemIndex) }))
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
            it.copy(buttonList = NotNullIngredientList(it.buttonList.values.toMutableList().apply { add(to.index, removeAt(from.index)) }))
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

    private fun onClickReset() {
        _uiState.update {
            it.copy(
                displayList = NullableIngredientList(emptyList()),
                selectedDisplayIndex = -1,
                typedText = ""
            )
        }
    }
}