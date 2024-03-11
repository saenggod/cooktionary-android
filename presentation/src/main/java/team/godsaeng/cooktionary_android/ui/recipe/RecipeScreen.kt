package team.godsaeng.cooktionary_android.ui.recipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import team.godsaeng.cooktionary_android.R
import team.godsaeng.cooktionary_android.ui.LoadingDialog
import team.godsaeng.cooktionary_android.ui.ScrapButton
import team.godsaeng.cooktionary_android.ui.StyledText
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.container.RECIPE_RECIPE_INDEX
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent.OnStarted
import team.godsaeng.cooktionary_android.ui.theme.PointColor
import team.godsaeng.cooktionary_android.ui.theme.RecipeDetailTextColor
import team.godsaeng.cooktionary_android.ui.theme.Typography
import team.godsaeng.domain.model.model.recipe.Recipe

private val LocalUiEvent = compositionLocalOf { { _: UiEvent -> } }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeScreen(
    navController: NavController,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val onEvent = remember { { event: UiEvent -> uiEvent(event) } }

    CompositionLocalProvider(LocalUiEvent provides onEvent) {
        val localUiEvent = LocalUiEvent.current

        LaunchedEffect(Unit) {
            navController.currentBackStackEntry?.arguments?.getInt(RECIPE_RECIPE_INDEX)?.let {
                localUiEvent(OnStarted(it))
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                LoadingDialog()
            } else {
                if (uiState.recipeList.values.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TopBar(
                            onClickBackButton = {},
                            middleContents = {},
                            onClickProfileIcon = {}
                        )

                        HorizontalPager(
                            state = rememberPagerState(
                                initialPage = uiState.startIndex,
                                initialPageOffsetFraction = 0f,
                                pageCount = { uiState.recipeList.values.size }
                            ),
                            modifier = Modifier.fillMaxSize(),
                        ) { index ->
                            RecipeDetail(uiState.recipeList.values[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeDetail(recipe: Recipe) {
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
            contentScale = Crop,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(18.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                StyledText(
                    text = recipe.title,
                    style = Typography.bodyMedium,
                    fontSize = 18
                )

                ScrapButton(onClick = {})
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = CenterVertically) {
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
    Row(verticalAlignment = CenterVertically) {
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
    Row(verticalAlignment = CenterVertically) {
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