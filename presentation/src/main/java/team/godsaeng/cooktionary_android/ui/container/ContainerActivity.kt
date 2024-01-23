package team.godsaeng.cooktionary_android.ui.container

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
import team.godsaeng.cooktionary_android.ui.main.MainScreen
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
        navScreen(Destination.ON_BOARDING.route) { OnBoardingScreen(navController) }
        navScreen(Destination.MAIN.route) { MainScreen(navController) }
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