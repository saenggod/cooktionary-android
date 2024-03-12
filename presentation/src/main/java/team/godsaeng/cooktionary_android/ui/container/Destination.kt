package team.godsaeng.cooktionary_android.ui.container

enum class Destination(val route: String) {
    ON_BOARDING(route = ROUTE_ON_BOARDING),
    MAIN(route = ROUTE_MAIN),
    SEARCH_RESULT(route = ROUTE_SEARCH_RESULT),
    MY_PAGE(route = ROUTE_MY_PAGE),
    MY_INFO(route = ROUTE_MY_INFO),
    SECESSION(route = ROUTE_SECESSION),
    SAVED_RECIPE_LIST(route = ROUTE_SAVED_RECIPE_LIST)
}

const val ROUTE_ON_BOARDING = "on_boarding"
const val ROUTE_MAIN = "main"
const val ROUTE_SEARCH_RESULT = "search_result"
const val ROUTE_MY_PAGE = "my_page"
const val ROUTE_MY_INFO = "my_info"
const val ROUTE_SECESSION = "secession"
const val ROUTE_SAVED_RECIPE_LIST = "saved_recipe_list"

const val SEARCH_RESULT_INGREDIENTS = "ingredients"