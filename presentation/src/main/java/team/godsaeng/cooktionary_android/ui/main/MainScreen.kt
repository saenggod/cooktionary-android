package team.godsaeng.cooktionary_android.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.alpha
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDrag
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDragStart
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDragStop
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnIngredientButtonMeasured
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.pxToDp
import team.godsaeng.cooktionary_android.ui.theme.DraggingGrey
import team.godsaeng.cooktionary_android.ui.theme.Grey0
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.SubColor
import team.godsaeng.cooktionary_android.ui.theme.TextColor
import team.godsaeng.cooktionary_android.ui.theme.Typography

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)

    LaunchedEffect(uiEffect) {
        uiEffect.collectLatest { uiEffect ->
            when (uiEffect) {
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection()

            DisplaySection(uiEvent)

            ButtonSection(
                ingredientButtonList = uiState.ingredientButtonList,
                ingredientButtonSize = uiState.ingredientButtonSize,
                uiEvent = uiEvent
            )
        }

        if (uiState.isDragging) {
            TrashCan(
                trashCanSize = uiState.ingredientButtonSize,
                isDeletable = uiState.isDeletable,
                uiEvent = uiEvent
            )

            uiState.draggingIngredient?.let { draggingIngredient ->
                DraggedIngredientButton(
                    ingredient = draggingIngredient,
                    ingredientButtonSize = uiState.ingredientButtonSize,
                    xPosition = uiState.draggedXPosition,
                    yPosition = uiState.draggedYPosition
                )
            }
        }
    }
}

@Composable
private fun TopSection() {
    TopBar(
        onClickProfileIcon = { /*TODO*/ },
        middleContents = {

        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColumnScope.DisplaySection(
    uiEvent: (UiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(2f)
    ) {
        val lazyRowState = rememberLazyListState()

        LazyRow(
            modifier = Modifier
                .align(Center)
                .fillMaxWidth(),
            state = lazyRowState,
            horizontalArrangement = spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 100.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyRowState)
        ) {
//            testList.forEachIndexed { index, s ->
//                if (index % 2 == 0) {
//                    item {
//                        IngredientDisplay()
//                    }
//                } else {
//                    item {
//                        Row(verticalAlignment = CenterVertically) {
//                            Icon(
//                                modifier = Modifier.size(20.dp),
//                                painter = painterResource(id = R.drawable.ic_plus),
//                                tint = SubColor,
//                                contentDescription = null
//                            )
//
//                            Spacer(modifier = Modifier.width(20.dp))
//
//                            IngredientDisplay()
//
//                            Spacer(modifier = Modifier.width(20.dp))
//
//                            if (index == testList.lastIndex) {
//                                Spacer(modifier = Modifier.width(40.dp))
//                            } else {
//                                Icon(
//                                    modifier = Modifier.size(20.dp),
//                                    painter = painterResource(id = R.drawable.ic_plus),
//                                    tint = SubColor,
//                                    contentDescription = null
//                                )
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}

@Composable
private fun IngredientDisplay() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(22.dp))
            .size(106.dp)
            .border(
                width = (1.15).dp,
                color = Color.Black.alpha(3),
                shape = RoundedCornerShape(22.dp)
            )
            .background(color = Grey0)
    ) {
        Icon(
            modifier = Modifier.align(Center),
            painter = painterResource(id = R.drawable.ic_plus),
            tint = SubColor,
            contentDescription = null
        )
    }
}

@Composable
private fun ColumnScope.ButtonSection(
    ingredientButtonList: List<String>,
    ingredientButtonSize: Int,
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
        FunctionButtonRow()

        IngredientsButtonSection(
            ingredientButtonList = ingredientButtonList,
            ingredientButtonSize = ingredientButtonSize,
            uiEvent = uiEvent
        )
    }
}

@Composable
private fun FunctionButtonRow() {
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
        ) {
            Image(
                modifier = Modifier.align(Center),
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .weight(3f)
                .clip(shape = RoundedCornerShape(12.dp))
                .fillMaxHeight()
                .background(color = PointColor)
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
private fun IngredientsButtonSection(
    ingredientButtonList: List<String>,
    ingredientButtonSize: Int,
    uiEvent: (UiEvent) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(top = 4.dp),
        horizontalArrangement = spacedBy(2.dp),
        verticalArrangement = spacedBy(2.dp)
    ) {
        items(
            ingredientButtonList,
            key = { it }
        ) {
            IngredientButton(
                ingredient = it,
                ingredientButtonSize = ingredientButtonSize,
                uiEvent = uiEvent
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyGridItemScope.IngredientButton(
    ingredient: String,
    ingredientButtonSize: Int,
    uiEvent: (UiEvent) -> Unit
) {
    var x by remember { mutableFloatStateOf(0f) }
    var y by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .width(0.dp)
            .aspectRatio(1f)
            .animateItemPlacement()
            .background(color = Grey0)
            .clickableWithoutRipple {
                // todo : onClick
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { start ->
                        uiEvent(
                            OnDragStart(
                                ingredient = ingredient,
                                startingXPosition = x + start.x,
                                startingYPosition = y + start.y
                            )
                        )
                    },
                    onDragEnd = {
                        uiEvent(OnDragStop)
                    },
                    onDrag = { _, amount ->
                        uiEvent(
                            OnDrag(
                                draggedXOffset = amount.x,
                                draggedYOffset = amount.y
                            )
                        )
                    }
                )
            }
            .onGloballyPositioned {
                x = it.positionInRoot().x
                y = it.positionInRoot().y

                if (ingredientButtonSize == 0) {
                    uiEvent(OnIngredientButtonMeasured(it.size.width))
                }
            }
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
private fun DraggedIngredientButton(
    ingredient: String,
    ingredientButtonSize: Int,
    xPosition: Float,
    yPosition: Float
) {
    Box(
        modifier = Modifier
            .size(pxToDp(ingredientButtonSize.toFloat()))
            .absoluteOffset(
                x = pxToDp(xPosition),
                y = pxToDp(yPosition)
            )
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = DraggingGrey.alpha(50))
    ) {
        Column(modifier = Modifier.align(Center)) {
            StyledText(
                text = ingredient,
                style = Typography.bodyMedium,
                fontSize = 13,
                color = TextColor
            )
        }
    }
}

@Composable
private fun BoxScope.TrashCan(
    trashCanSize: Int,
    isDeletable: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .align(BottomCenter)
            .padding(bottom = 24.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .size(pxToDp(trashCanSize.toFloat()))
            .background(color = Color.Red.alpha(if (isDeletable) 100 else 50))
            .onGloballyPositioned {
                uiEvent(
                    OnTrashCanMeasured(
                        xPosition = it.positionInRoot().x,
                        yPosition = it.positionInRoot().y
                    )
                )
            }
    )
}