package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
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
import kotlinx.coroutines.flow.collectLatest
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
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEffect.ScrollToEndOfDisplayList
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickAddIngredientButton
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickAddIngredientDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnClickRemoveIngredientDisplay
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDragged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDraggingEnded
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnIngredientTyped
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnOrderChanged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTypingIngredientEnded
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
    val displayListState = rememberLazyListState()

    LaunchedEffect(uiEffect) {
        uiEffect.collectLatest { uiEffect ->
            when (uiEffect) {
                is ScrollToEndOfDisplayList -> displayListState.animateScrollToItem(uiEffect.targetIndex)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection(uiState.addedIngredientCount)

            DisplaySection(
                displayListState = displayListState,
                ingredientDisplayList = uiState.ingredientDisplayList,
                typedIngredient = uiState.typedIngredient,
                uiEvent = uiEvent,
            )

            ButtonSection(
                ingredientButtonList = uiState.ingredientButtonList,
                uiEvent = uiEvent
            )
        }

        if (uiState.isDragging) {
            TrashCan(
                isRemovable = uiState.isRemovable,
                uiEvent = uiEvent
            )
        }
    }
}

@Composable
private fun TopSection(
    ingredientCount: Int
) {
    TopBar(
        onClickProfileIcon = { /*TODO*/ },
        middleContents = {
            Row(verticalAlignment = CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    tint = AddedIngredientDescColor,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                StyledText(
                    text = stringResource(id = R.string.added_ingredient_count, ingredientCount),
                    style = Typography.bodyMedium,
                    color = AddedIngredientDescColor,
                    fontSize = 12
                )
            }
        }
    )
}

const val IngredientDisplaySize = 106

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.DisplaySection(
    displayListState: LazyListState,
    ingredientDisplayList: List<String?>,
    typedIngredient: String,
    uiEvent: (UiEvent) -> Unit
) {
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
            state = displayListState,
            horizontalArrangement = spacedBy(30.dp),
            verticalAlignment = CenterVertically,
            contentPadding = PaddingValues(horizontal = (screenWidth / 2 - (IngredientDisplaySize + 8) / 2).dp),
            flingBehavior = rememberSnapFlingBehavior(displayListState)
        ) {
            ingredientDisplayList.forEachIndexed { index, ingredient ->
                item {
                    IngredientDisplay(
                        ingredient = ingredient,
                        typedIngredient = typedIngredient,
                        index = index,
                        uiEvent = uiEvent
                    )
                }

                if (index + 1 <= ingredientDisplayList.lastIndex) {
                    item {
                        PlusIcon()
                    }
                }
            }
        }
    }
}

@Composable
private fun IngredientDisplay(
    ingredient: String?,
    typedIngredient: String,
    index: Int,
    uiEvent: (UiEvent) -> Unit
) {
    var hasFocus by remember { mutableStateOf(false) }

    Box(modifier = Modifier.size((IngredientDisplaySize + 8).dp)) {
        Box(
            modifier = Modifier
                .align(Center)
                .clip(shape = RoundedCornerShape(22.dp))
                .size(IngredientDisplaySize.dp)
                .border(
                    width = (1.15).dp,
                    color = Color.Black.alpha(3),
                    shape = RoundedCornerShape(22.dp)
                )
                .background(color = Grey0)
        ) {
            ingredient?.let {

            } ?: run {
                val focusManager = LocalFocusManager.current

                SimpleTextField(
                    modifier = Modifier
                        .align(Center)
                        .onFocusEvent { hasFocus = it.hasFocus },
                    value = typedIngredient,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            uiEvent(OnTypingIngredientEnded(index))
                        }
                    ),
                    onValueChange = { uiEvent(OnIngredientTyped(it)) }
                )

                if (!hasFocus && typedIngredient.isEmpty()) {
                    PlusIcon(modifier = Modifier.align(Center))
                }
            }
        }

        if (hasFocus) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickableWithoutRipple { uiEvent(OnClickRemoveIngredientDisplay(index)) },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ColumnScope.ButtonSection(
    ingredientButtonList: List<String>,
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
            ingredientButtonList = ingredientButtonList,
            uiEvent = uiEvent
        )

        IngredientsButtonSection(
            ingredientButtonList = ingredientButtonList,
            uiEvent = uiEvent
        )
    }
}

@Composable
private fun FunctionButtonRow(
    ingredientButtonList: List<String>,
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
                .clickableWithoutRipple { uiEvent(OnClickAddIngredientDisplay(null)) },
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
                .background(color = if (ingredientButtonList.isEmpty()) Grey1 else PointColor)
                .clickableWithoutRipple { }
        ) {
            StyledText(
                modifier = Modifier.align(Center),
                stringId = R.string.search_recipe,
                style = Typography.titleLarge,
                fontSize = 14,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ColumnScope.IngredientsButtonSection(
    ingredientButtonList: List<String>,
    uiEvent: (UiEvent) -> Unit
) {
    val reorderableLazyGridState = rememberReorderableLazyGridState(
        onMove = { from, to ->
            uiEvent(
                OnOrderChanged(
                    from = from,
                    to = to
                )
            )
        },
        onDragEnd = { _, to ->
            uiEvent(OnDraggingEnded(to))
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .reorderable(reorderableLazyGridState)
                .detectReorderAfterLongPress(reorderableLazyGridState),
            state = reorderableLazyGridState.gridState,
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(top = 4.dp),
            horizontalArrangement = spacedBy(2.dp),
            verticalArrangement = spacedBy(2.dp)
        ) {
            items(
                items = ingredientButtonList,
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

        if (ingredientButtonList.isEmpty()) {
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
                    .clickableWithoutRipple { uiEvent(OnClickAddIngredientButton) }
                    .pointerInteropFilter {
                        startingXPosition = it.x
                        startingYPosition = it.y
                        false
                    }
            },
            onTrue = { modifier ->
                modifier.onGloballyPositioned {
                    uiEvent(OnDragged(it.positionInRoot() + Offset(startingXPosition, startingYPosition)))
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
    isRemovable: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .align(BottomCenter)
            .padding(bottom = 24.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .size(48.dp)
            .background(color = Color.Red.alpha(if (isRemovable) 100 else 50))
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