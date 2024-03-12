package team.godsaeng.cooktionary_android.ui.container

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import team.godsaeng.cooktionary_android.ui.main.MainScreen
import team.godsaeng.cooktionary_android.ui.my_info.MyInfoScreen
import team.godsaeng.cooktionary_android.ui.my_page.MyPageScreen
import team.godsaeng.cooktionary_android.ui.on_boarding.OnBoardingScreen
import team.godsaeng.cooktionary_android.ui.saved_recipe_list.SavedRecipeListScreen
import team.godsaeng.cooktionary_android.ui.search_result.SearchResultScreen
import team.godsaeng.cooktionary_android.ui.secession.SecessionScreen
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
            navScreen(
                destination = "${Destination.SEARCH_RESULT.route}/{$SEARCH_RESULT_INGREDIENTS}",
                arguments = listOf(navArgument(SEARCH_RESULT_INGREDIENTS) { type = NavType.StringType })
            ) { SearchResultScreen(this) }
            navScreen(Destination.MY_PAGE.route) { MyPageScreen(this) }
            navScreen(Destination.MY_INFO.route) { MyInfoScreen(this) }
            navScreen(Destination.SECESSION.route) { SecessionScreen(this) }
            navScreen(Destination.SAVED_RECIPE_LIST.route) { SavedRecipeListScreen(this) }
        }
    }
}

fun NavGraphBuilder.navScreen(
    destination: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable () -> Unit
) {
    composable(
        route = destination,
        arguments = arguments,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) { content() }
}

fun buildInclusivePopUpOption(route: String): NavOptions = NavOptions.Builder()
    .setPopUpTo(
        route = route,
        inclusive = true
    )
    .build()
