package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.SimpleTextField
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.alpha
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.branchedModifier
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect.ClearFocus
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonDragged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonDraggingEnded
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnButtonOrderChanged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickAddDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickRemoveDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickReset
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDone
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTyped
import team.godsaeng.cooktionary_android.ui.theme.AddedIngredientDescColor
import team.godsaeng.cooktionary_android.ui.theme.Grey0
import team.godsaeng.cooktionary_android.ui.theme.Grey1
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.SubColor
import team.godsaeng.cooktionary_android.ui.theme.Typography

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiEffect) {
        // repeatedOnStart
        uiEffect.collect { uiEffect ->
            when (uiEffect) {
                is ClearFocus -> focusManager.clearFocus()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection(
                displayCount = uiState.displayList.size,
                uiEvent = uiEvent
            )

            DisplaySection(
                displayList = uiState.displayList,
                selectedDisplayIndex = uiState.selectedDisplayIndex,
                typedText = uiState.typedText,
                uiEvent = uiEvent,
            )

            ButtonSection(
                displayList = uiState.displayList,
                buttonList = uiState.buttonList,
                uiEvent = uiEvent
            )
        }

        if (uiState.isButtonDragging) {
            TrashCan(
                isButtonRemovable = uiState.isButtonRemovable,
                uiEvent = uiEvent
            )
        }
    }
}

@Composable
private fun TopSection(
    displayCount: Int,
    uiEvent: (UiEvent) -> Unit
) {
    TopBar(
        onClickProfileIcon = { /*TODO*/ },
        middleContents = {
            Row(verticalAlignment = CenterVertically) {
                Icon(
                    modifier = Modifier.clickableWithoutRipple { uiEvent(OnClickReset) },
                    painter = painterResource(id = R.drawable.ic_reset),
                    tint = AddedIngredientDescColor,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                StyledText(
                    text = stringResource(id = R.string.added_ingredient_count, displayCount),
                    style = Typography.bodyMedium,
                    color = AddedIngredientDescColor,
                    fontSize = 12
                )
            }
        }
    )
}

const val IngredientDisplaySize = 114

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.DisplaySection(
    displayList: List<String?>,
    selectedDisplayIndex: Int,
    typedText: String,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
    ) {
        LazyRow(
            modifier = Modifier
                .align(Center)
                .fillMaxWidth(),
            state = lazyListState,
            horizontalArrangement = spacedBy(30.dp),
            verticalAlignment = CenterVertically,
            contentPadding = PaddingValues(horizontal = ((screenWidth - IngredientDisplaySize) / 2).dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState)
        ) {
            displayList.forEachIndexed { index, ingredient ->
                val displayIndex = index * 2

                item {
                    IngredientDisplay(
                        index = displayIndex,
                        ingredient = ingredient,
                        selectedDisplayIndex = selectedDisplayIndex,
                        typedText = typedText,
                        uiEvent = uiEvent
                    )
                }

                if (index < displayList.lastIndex) {
                    item {
                        PlusIcon()
                    }
                }
            }
        }
    }

    LaunchedEffect(selectedDisplayIndex) {
        if (selectedDisplayIndex >= 0) {
            lazyListState.animateScrollToItem(selectedDisplayIndex)
        }
    }
}

@Composable
private fun IngredientDisplay(
    index: Int,
    ingredient: String?,
    selectedDisplayIndex: Int,
    typedText: String,
    uiEvent: (UiEvent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val isSelected = index == selectedDisplayIndex

    LaunchedEffect(selectedDisplayIndex) {
        if (isSelected) {
            delay(50L)
            focusRequester.requestFocus()
        }
    }

    Box(modifier = Modifier.size(IngredientDisplaySize.dp)) {
        Box(
            modifier = Modifier
                .align(Center)
                .padding(4.dp)
                .clip(shape = RoundedCornerShape(22.dp))
                .fillMaxSize()
                .border(
                    width = (1.15).dp,
                    color = Color.Black.alpha(3),
                    shape = RoundedCornerShape(22.dp)
                )
                .background(color = Grey0)
                .clickableWithoutRipple {
                    if (index != selectedDisplayIndex) {
                        uiEvent(
                            OnClickDisplay(
                                index = index,
                                ingredient = ingredient
                            )
                        )
                    }
                }
        ) {
            Box(modifier = Modifier.align(Center)) {
                SimpleTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                if (index != selectedDisplayIndex) {
                                    uiEvent(
                                        OnClickDisplay(
                                            index = index,
                                            ingredient = ingredient
                                        )
                                    )
                                }
                            }
                        },
                    value = if (isSelected) typedText else ingredient.orEmpty(),
                    keyboardActions = KeyboardActions(onDone = { uiEvent(OnDone(index / 2)) }),
                    onValueChange = { uiEvent(OnTyped(it)) }
                )
            }

            if (!isSelected && ingredient == null)
                PlusIcon(modifier = Modifier.align(Center))
        }

        if (isSelected) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickableWithoutRipple { uiEvent(OnClickRemoveDisplay(index / 2)) },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ColumnScope.ButtonSection(
    displayList: List<String?>,
    buttonList: List<String>,
    uiEvent: (UiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .weight(3f)
            .background(color = Grey0.alpha(60))
    ) {
        FunctionButtonRow(
            displayList = displayList,
            uiEvent = uiEvent
        )

        IngredientsButtonSection(
            buttonList = buttonList,
            uiEvent = uiEvent
        )
    }
}

@Composable
private fun FunctionButtonRow(
    displayList: List<String?>,
    uiEvent: (UiEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(shape = RoundedCornerShape(12.dp))
                .fillMaxHeight()
                .background(color = SubColor)
                .clickableWithoutRipple { uiEvent(OnClickAddDisplay) },
        ) {
            Icon(
                modifier = Modifier.align(Center),
                painter = painterResource(id = R.drawable.ic_plus),
                tint = Color.White,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .weight(2.5f)
                .clip(shape = RoundedCornerShape(12.dp))
                .fillMaxHeight()
                .background(color = if (displayList.isEmpty() || displayList.contains(null)) Grey1 else PointColor)
                .clickableWithoutRipple { }
        ) {
            StyledText(
                modifier = Modifier.align(Center),
                stringId = R.string.search_recipe,
                style = Typography.bodyLarge,
                fontSize = 14,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ColumnScope.IngredientsButtonSection(
    buttonList: List<String>,
    uiEvent: (UiEvent) -> Unit
) {
    val reorderableLazyGridState = rememberReorderableLazyGridState(
        onMove = { from, to ->
            uiEvent(
                OnButtonOrderChanged(
                    from = from,
                    to = to
                )
            )
        },
        onDragEnd = { _, to ->
            uiEvent(OnButtonDraggingEnded(to))
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .reorderable(reorderableLazyGridState)
                .detectReorderAfterLongPress(reorderableLazyGridState),
            state = reorderableLazyGridState.gridState,
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(top = 4.dp),
            horizontalArrangement = spacedBy(2.dp),
            verticalArrangement = spacedBy(2.dp)
        ) {
            items(
                items = buttonList,
                key = { it }
            ) { item ->
                ReorderableItem(
                    reorderableState = reorderableLazyGridState,
                    key = item
                ) { isDragging ->
                    IngredientButton(
                        ingredient = item,
                        isDragging = isDragging,
                        uiEvent = uiEvent
                    )
                }
            }
        }

        if (buttonList.isEmpty()) {
            StyledText(
                modifier = Modifier.align(Center),
                style = Typography.bodyMedium.copy(textAlign = TextAlign.Center),
                lineHeight = 1.4f,
                stringId = R.string.no_ingredient_desc,
                fontSize = 14
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun LazyGridItemScope.IngredientButton(
    ingredient: String,
    isDragging: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    var startingXPosition by remember { mutableFloatStateOf(0f) }
    var startingYPosition by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = branchedModifier(
            condition = isDragging,
            onDefault = {
                Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .aspectRatio(1f)
                    .animateItemPlacement()
                    .background(color = Grey0)
                    .clickableWithoutRipple { }
                    .pointerInteropFilter {
                        startingXPosition = it.x
                        startingYPosition = it.y
                        false
                    }
            },
            onTrue = { modifier ->
                modifier.onGloballyPositioned {
                    uiEvent(OnButtonDragged(it.positionInRoot() + Offset(startingXPosition, startingYPosition)))
                }
            },
        )
    ) {
        Column(modifier = Modifier.align(Center)) {
            StyledText(
                text = ingredient,
                style = Typography.bodyMedium,
                fontSize = 13
            )
        }
    }
}

@Composable
private fun BoxScope.TrashCan(
    isButtonRemovable: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .align(BottomCenter)
            .padding(bottom = 24.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .size(48.dp)
            .background(color = Color.Red.alpha(if (isButtonRemovable) 100 else 50))
            .onGloballyPositioned {
                uiEvent(
                    OnTrashCanMeasured(
                        trashCanSize = it.size.width,
                        trashCanPosition = it.positionInRoot()
                    )
                )
            }
    )
}

@Composable
private fun PlusIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_plus),
        tint = SubColor,
        contentDescription = null
    )
}