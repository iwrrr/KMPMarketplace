
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(val route: String) {
    data object Home : Routes(route = "/home")
    data object ProductList : Routes(route = "/product/list/{$PRODUCT_LIST_ARG}")
    data object ProductDetail : Routes(route = "/product/{$PRODUCT_ID}")
    data object Favorite : Routes(route = "/favorite")
}

enum class TopLevelRoute(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(
        label = "Home",
        route = Routes.Home.route,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    FAVORITE(
        label = "Favorite",
        route = Routes.Favorite.route,
        selectedIcon = Icons.Rounded.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    ),
}

const val PRODUCT_LIST_ARG = "argument"
const val PRODUCT_ID = "product_id"