package team.godsaeng.cooktionary_android.ui.container

enum class Destination(val route: String) {
    ON_BOARDING(route = ROUTE_ON_BOARDING),
    MAIN(route = ROUTE_MAIN)
}

const val ROUTE_ON_BOARDING = "on_boarding"
const val ROUTE_MAIN = "main"