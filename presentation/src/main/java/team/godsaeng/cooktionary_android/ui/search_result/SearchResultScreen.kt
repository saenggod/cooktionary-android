package team.godsaeng.cooktionary_android.ui.search_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.model.wrapper.recipe.RecipeList
import team.godsaeng.cooktionary_android.ui.LoadingDialog
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.clickableWithoutRipple
import team.godsaeng.cooktionary_android.ui.container.SEARCH_RESULT_INGREDIENTS
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnClickRecipe
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnRefreshed
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.theme.ImagePlaceHolderColor
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.ProgressBackgroundColor
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey1
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey2
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey3
import team.godsaeng.cooktionary_android.ui.theme.TextColorGrey6
import team.godsaeng.cooktionary_android.ui.theme.Typography
import team.godsaeng.domain.model.model.recipe.Recipe

private val LocalUiEvent = compositionLocalOf { { _: UiEvent -> } }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchResultScreen(
    navController: NavController,
    viewModel: SearchResultViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val onEvent = remember { { event: UiEvent -> uiEvent(event) } }

    CompositionLocalProvider(LocalUiEvent provides onEvent) {
        val localUiEvent = LocalUiEvent.current

        LaunchedEffect(Unit) {
            navController.currentBackStackEntry?.arguments?.getString(SEARCH_RESULT_INGREDIENTS)?.let {
                localUiEvent(OnStarted(it.split(",")))
            }
        }

        val pullRefreshState = rememberPullRefreshState(
            refreshing = uiState.isRefreshing,
            onRefresh = { localUiEvent(OnRefreshed) },
            refreshThreshold = 80.dp
        )

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background)
            ) {
                TopSection(
                    userIngredientNames = uiState.userIngredientNameList.joinToString(", ")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.pullRefresh(pullRefreshState)) {
                    PullToRefreshSection(
                        fraction = pullRefreshState.progress.coerceIn(0f..1f)
                    )

                    SearchResultDescSection(
                        recipeCount = uiState.recipeList.values.size
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SearchResultSection(
                        recipeList = uiState.recipeList
                    )
                }
            }

            if (uiState.isLoading) {
                LoadingDialog()
            }
        }
    }
}

@Composable
private fun TopSection(
    userIngredientNames: String
) {
    TopBar(
        onClickBackButton = {

        },
        middleContents = {
            StyledText(
                text = userIngredientNames,
                style = Typography.bodyMedium,
                fontSize = 16,
                color = TextColorGrey1
            )
        },
        onClickProfileIcon = {

        }
    )

    ProgressBar(50)
}

@Composable
private fun PullToRefreshSection(
    fraction: Float
) {
    if (fraction != 0f) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(lerp(0.dp, 80.dp, fraction))
        ) {
            Column(
                modifier = Modifier.align(Center),
                horizontalAlignment = CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pull_refresh),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(8.dp))

                StyledText(
                    stringId = R.string.load_more_recipe,
                    style = Typography.bodyLarge,
                    fontSize = 14,
                    color = TextColorGrey6
                )
            }
        }
    }
}

@Composable
private fun ProgressBar(progress: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(color = ProgressBackgroundColor)
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp

        Box(
            modifier = Modifier
                .width((screenWidth / 100 * progress).dp)
                .fillMaxHeight()
                .background(color = PointColor)
        )
    }
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
        verticalArrangement = spacedBy(18.dp)
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
                contentScale = Crop,
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

            Image(
                modifier = Modifier.padding(6.dp),
                painter = painterResource(R.drawable.ic_scrap),
                contentDescription = null
            )
        }
    }
}