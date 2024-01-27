package team.godsaeng.cooktionary_android.ui.container

import SearchResultScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import team.godsaeng.cooktionary_android.ui.RecipeScreen
import team.godsaeng.cooktionary_android.ui.main.MainScreen
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoScreen
import team.godsaeng.cooktionary_android.ui.my_page.MyPageScreen
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingScreen
import team.godsaeng.cooktionary_android.ui.theme.CooktionaryandroidTheme

@AndroidEntryPoint
class ContainerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CooktionaryandroidTheme {
                ContainerScreen()
            }
        }
    }
}

@Composable
private fun ContainerScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.ON_BOARDING.route
    ) {
        with(navController) {
            navScreen(Destination.ON_BOARDING.route) { OnBoardingScreen(this) }
            navScreen(Destination.MAIN.route) { MainScreen(this) }
            navScreen(Destination.SEARCH_RESULT.route) { SearchResultScreen(this) }
            navScreen(Destination.RECIPE.route) { RecipeScreen(this) }
            navScreen(Destination.MY_PAGE.route) { MyPageScreen(this) }
            navScreen(Destination.MY_INFO.route) { MyInfoScreen(this) }
        }
    }
}

fun NavGraphBuilder.navScreen(
    destination: String,
    content: @Composable () -> Unit
) {
    composable(
        route = destination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) { content() }
}