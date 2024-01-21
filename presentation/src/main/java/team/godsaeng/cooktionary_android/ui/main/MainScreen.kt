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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.alpha
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.branchedModifier
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDrag
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnDragEnd
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnOrderChanged
import team.godsaeng.cooktionary_android.ui.main.MainContract.UiEvent.OnTrashCanMeasured
import team.godsaeng.cooktionary_android.ui.theme.Grey0
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.SubColor
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
                uiEvent = uiEvent
            )
        }

        if (uiState.isDragging) {
            TrashCan(
                isDeletable = uiState.isDeletable,
                uiEvent = uiEvent
            )
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
            flingBehavior = rememberSnapFlingBehavior(lazyRowState)
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
                .weight(2f)
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
            uiEvent(OnDragEnd(to))
        }
    )

    Box(modifier = Modifier.weight(1f)) {
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
                    .clickableWithoutRipple {
                        // todo : onClick
                    }
                    .pointerInteropFilter {
                        startingXPosition = it.x
                        startingYPosition = it.y
                        false
                    }
            },
            onTrue = { modifier ->
                modifier.onGloballyPositioned {
                    uiEvent(OnDrag(it.positionInRoot() + Offset(startingXPosition, startingYPosition)))
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
    isDeletable: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .align(BottomCenter)
            .padding(bottom = 24.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .size(48.dp)
            .background(color = Color.Red.alpha(if (isDeletable) 100 else 50))
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