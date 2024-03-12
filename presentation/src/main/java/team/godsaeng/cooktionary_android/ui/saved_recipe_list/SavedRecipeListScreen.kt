package team.godsaeng.cooktionary_android.ui.saved_recipe_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.CollectUiEffectWithLifecycle
import team.godsaeng.cooktionary_android.ui.LoadingDialog
import team.godsaeng.cooktionary_android.ui.ScrapButton
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.container.ROUTE_MAIN
import team.godsaeng.cooktionary_android.ui.container.ROUTE_MY_PAGE
import team.godsaeng.cooktionary_android.ui.container.ROUTE_ON_BOARDING
import team.godsaeng.cooktionary_android.ui.container.buildInclusivePopUpOption
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEffect.GoToProfile
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnBackPressed
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnClickProfile
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnClickRecipe
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnClickSaveRecipe
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.theme.ImagePlaceHolderColor
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.RecipeDetailTextColor
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey1
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey2
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey3
import team.godsaeng.cooktionary_android.ui.theme.Typography
import team.godsaeng.domain.model.model.recipe.Recipe

private val LocalUiEvent = compositionLocalOf { { _: UiEvent -> } }

@Composable
fun SavedRecipeListScreen(
    navController: NavController,
    viewModel: SavedRecipeListViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val onEvent = remember { { event: UiEvent -> uiEvent(event) } }

    CollectUiEffectWithLifecycle(uiEffect) { collected ->
        when (collected) {
            is GoToProfile -> navController.navigate(
                route = ROUTE_MY_PAGE,
                navOptions = buildInclusivePopUpOption(ROUTE_MY_PAGE)
            )
        }
    }

    CompositionLocalProvider(LocalUiEvent provides onEvent) {
        val localUiEvent = LocalUiEvent.current

        LaunchedEffect(Unit) {
            localUiEvent(OnStarted)
        }

        Box {
            if (uiState.isPagerMode) {
                RecipePagerScreen(
                    recipeList = uiState.recipeList,
                    startIndex = uiState.startIndex
                )

                BackHandler {
                    localUiEvent(OnBackPressed)
                }
            } else {
                RecipeListScreen(
                    recipeList = uiState.recipeList
                )
            }

            if (uiState.isLoading) {
                LoadingDialog()
            }
        }
    }
}

@Composable
private fun RecipeListScreen(
    recipeList: RecipeList
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        TopSection()

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            SearchResultDescSection(
                recipeCount = recipeList.values.size
            )

            Spacer(modifier = Modifier.height(20.dp))

            SearchResultSection(
                recipeList = recipeList
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecipePagerScreen(
    recipeList: RecipeList,
    startIndex: Int
) {
    val localUiEvent = LocalUiEvent.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (recipeList.values.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopBar(
                    onClickBackButton = {},
                    middleContents = {},
                    onClickProfileIcon = { localUiEvent(OnClickProfile) }
                )

                HorizontalPager(
                    state = rememberPagerState(
                        initialPage = startIndex,
                        initialPageOffsetFraction = 0f,
                        pageCount = { recipeList.values.size }
                    ),
                    modifier = Modifier.fillMaxSize(),
                ) { index ->
                    RecipeDetail(recipeList.values[index])
                }
            }
        }
    }
}

@Composable
private fun TopSection() {
    val localUiEvent = LocalUiEvent.current

    TopBar(
        onClickBackButton = {

        },
        middleContents = {
            StyledText(
                text = "저장된 레시피",
                style = Typography.bodyMedium,
                fontSize = 16,
                color = TextColorGrey1
            )
        },
        onClickProfileIcon = { localUiEvent(OnClickProfile) }
    )
}

@Composable
private fun SearchResultDescSection(recipeCount: Int) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        StyledText(
            text = stringResource(R.string.result_count, recipeCount),
            style = Typography.bodyMedium,
            fontSize = 14,
            color = TextColorGrey1
        )

        Spacer(modifier = Modifier.height(4.dp))

        StyledText(
            stringId = R.string.result_desc,
            style = Typography.bodyMedium,
            fontSize = 12,
            color = TextColorGrey2
        )
    }
}

@Composable
private fun SearchResultSection(
    recipeList: RecipeList
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Absolute.spacedBy(18.dp)
    ) {
        itemsIndexed(recipeList.values) { index, recipe ->
            Recipe(
                index = index,
                recipe = recipe
            )
        }
    }
}

@Composable
private fun Recipe(
    index: Int,
    recipe: Recipe
) {
    val localUiEvent = LocalUiEvent.current

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(70.dp)
            .clickableWithoutRipple { localUiEvent(OnClickRecipe(index)) }
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .size(70.dp)
                .background(color = ImagePlaceHolderColor)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = recipe.imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StyledText(
                    text = recipe.title,
                    style = Typography.bodyLarge,
                    fontSize = 18,
                    color = Color.Black
                )

                StyledText(
                    text = stringResource(id = R.string.cooking_time, recipe.time),
                    style = Typography.bodyLarge,
                    fontSize = 13,
                    color = TextColorGrey3
                )

                StyledText(
                    text = if (recipe.neededIngredientCount == 0) stringResource(id = R.string.cookable_right_now)
                    else stringResource(id = R.string.needed_other_ingredients_with_count, recipe.neededIngredientCount),
                    style = Typography.bodyLarge,
                    fontSize = 13,
                    color = if (recipe.neededIngredientCount == 0) PointColor else TextColorGrey3
                )
            }

            ScrapButton(
                isSaved = recipe.isSaved,
                onClick = {
                    localUiEvent(
                        OnClickSaveRecipe(
                            recipeId = recipe.id,
                            isSaved = recipe.isSaved
                        )
                    )
                }
            )
        }
    }
}


@Composable
private fun RecipeDetail(recipe: Recipe) {
    val localUiEvent = LocalUiEvent.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colors.background)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            model = recipe.imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(18.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StyledText(
                    text = recipe.title,
                    style = Typography.bodyMedium,
                    fontSize = 18
                )

                ScrapButton(
                    isSaved = recipe.isSaved,
                    onClick = {
                        localUiEvent(
                            OnClickSaveRecipe(
                                recipeId = recipe.id,
                                isSaved = recipe.isSaved
                            )
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                DifficultySection(recipe.difficultyToLevel())

                Spacer(modifier = Modifier.width(16.dp))

                TimeSection(recipe.time)
            }

            Spacer(modifier = Modifier.height(24.dp))

            StyledText(
                text = recipe.content,
                style = Typography.bodyMedium,
                fontSize = 14,
                color = RecipeDetailTextColor,
                lineHeight = 1.5f
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DifficultySection(difficulty: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StyledText(
            stringId = R.string.difficulty,
            style = Typography.bodyMedium,
            fontSize = 12,
            color = PointColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        repeat(3 - difficulty) {
            Image(
                painter = painterResource(R.drawable.ic_star_empty),
                contentDescription = null
            )
        }

        repeat(difficulty) {
            Image(
                painter = painterResource(R.drawable.ic_star_filled),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun TimeSection(time: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StyledText(
            stringId = R.string.cooking_time,
            style = Typography.bodyMedium,
            fontSize = 12,
            color = PointColor
        )

        Spacer(modifier = Modifier.width(4.dp))

        Image(
            painter = painterResource(R.drawable.ic_time),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(1.5.dp))

        StyledText(
            text = time,
            style = Typography.bodyMedium,
            fontSize = 10,
            color = PointColor
        )
    }
}