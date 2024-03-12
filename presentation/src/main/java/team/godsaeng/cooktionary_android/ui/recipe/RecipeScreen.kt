package team.godsaeng.cooktionary_android.ui.recipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import team.godsaeng.cooktionary_android.ui.CollectUiEffectWithLifecycle
import team.godsaeng.cooktionary_android.ui.LoadingDialog
import team.godsaeng.cooktionary_android.ui.TopBar
import team.godsaeng.cooktionary_android.ui.base.use
import team.godsaeng.cooktionary_android.ui.container.ROUTE_MY_PAGE
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEffect.GoToProfile
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent
import team.godsaeng.cooktionary_android.ui.recipe.RecipeContract.UiEvent.OnClickProfile

private val LocalUiEvent = compositionLocalOf { { _: UiEvent -> } }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeScreen(
    navController: NavController,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    val (uiState, uiEvent, uiEffect) = use(viewModel)
    val onEvent = remember { { event: UiEvent -> uiEvent(event) } }

    CollectUiEffectWithLifecycle(uiEffect) { collected ->
        when (collected) {
            is GoToProfile -> navController.navigate(ROUTE_MY_PAGE)
        }
    }

    CompositionLocalProvider(LocalUiEvent provides onEvent) {
        val localUiEvent = LocalUiEvent.current

        LaunchedEffect(Unit) {
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (uiState.isLoading) {
                LoadingDialog()
            } else {
                if (uiState.recipeList.values.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TopBar(
                            onClickBackButton = {},
                            middleContents = {},
                            onClickProfileIcon = { localUiEvent(OnClickProfile) }
                        )

                        HorizontalPager(
                            state = rememberPagerState(
                                initialPage = uiState.startIndex,
                                initialPageOffsetFraction = 0f,
                                pageCount = { uiState.recipeList.values.size }
                            ),
                            modifier = Modifier.fillMaxSize(),
                        ) { index ->
                        }
                    }
                }
            }
        }
    }
}