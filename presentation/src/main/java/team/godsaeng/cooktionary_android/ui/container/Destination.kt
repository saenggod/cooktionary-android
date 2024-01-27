package team.godsaeng.cooktionary_android.ui.container

enum class Destination(val route: String) {
    ON_BOARDING(route = ROUTE_ON_BOARDING),
    MAIN(route = ROUTE_MAIN),
    SEARCH_RESULT(route = ROUTE_SEARCH_RESULT),
    RECIPE(route = ROUTE_RECIPE)
}

const val ROUTE_ON_BOARDING = "on_boarding"
const val ROUTE_MAIN = "main"
const val ROUTE_SEARCH_RESULT = "search_result"
const val ROUTE_RECIPE = "recipe"