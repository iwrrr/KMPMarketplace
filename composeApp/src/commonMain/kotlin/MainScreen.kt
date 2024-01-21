import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.features.favorite.screen.Favorite
import com.kmp.features.home.screen.Home
import com.kmp.features.product_list.ProductListArgument
import com.kmp.libraries.component.utils.toJson
import kotlinx.coroutines.launch

@Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    appState: AppState
) {
    val pagerState = rememberPagerState { appState.topLevelDestinations.size }
    val tabNavigator = LocalTabNavigator.current

    when (pagerState.currentPage) {
        0 -> tabNavigator.currentTab = TopLevelRoute.HOME
        1 -> tabNavigator.currentTab = TopLevelRoute.FAVORITE
    }

    val navigator = appState.navigator

    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomBar(
                destinations = appState.topLevelDestinations,
                tabNavigator = tabNavigator,
                navigateToTopLevelRoute = {
                    scope.launch {
                        pagerState.scrollToPage(it)
                    }
                }
            )
        }
    ) {
        HorizontalPager(
            modifier = Modifier.padding(
                WindowInsets.navigationBars.asPaddingValues()
            ),
            state = pagerState,
            beyondBoundsPageCount = appState.topLevelDestinations.size,
            userScrollEnabled = false
        ) { index ->
            when (index) {
                0 -> {
                    Home(
                        navigateToProductDetail = { product ->
                            val productId = product.id.toString()

                            navigator.navigate(Routes.ProductDetail.route.withArgument(productId))
                        },
                        navigateToProductList = {
                            val argument = ProductListArgument(
                                categoryId = it.id,
                                categoryName = it.name
                            ).toJson()

                            navigator.navigate(Routes.ProductList.route.withArgument(argument))
                        },
                        navigateToCart = {
                            navigator.navigate(Routes.Cart.route)
                        }
                    )
                }

                1 -> {
                    Favorite(
                        navigateToProductDetail = { product ->
                            val productId = product.id.toString()

                            navigator.navigate(Routes.ProductDetail.route.withArgument(productId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    destinations: List<TopLevelRoute>,
    tabNavigator: TabNavigator,
    navigateToTopLevelRoute: (Int) -> Unit,
) {
    BottomAppBar(
        backgroundColor = Color.White,
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        BottomNavigation(
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            destinations.forEachIndexed { index, topLevelRoute ->
                val selected by derivedStateOf { tabNavigator.currentTab == topLevelRoute }

                BottomNavigationItem(
                    label = {
                        Text(topLevelRoute.label)
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) topLevelRoute.selectedIcon else topLevelRoute.unselectedIcon,
                            contentDescription = null
                        )
                    },
                    selected = selected,
                    onClick = {
                        navigateToTopLevelRoute.invoke(index)
                    }
                )
            }
        }
    }
}

class TabNavigator {
    var currentTab by mutableStateOf(TopLevelRoute.HOME)
}

val LocalTabNavigator = compositionLocalOf<TabNavigator> { error("TabNavigator not provided") }