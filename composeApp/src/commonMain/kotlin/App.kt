import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.api.product.LocalProductRepository
import com.kmp.api.product.ProductRepository
import com.kmp.libraries.core.LocalAppConfig
import com.kmp.libraries.core.viewmodel.LocalViewModelHost
import com.kmp.libraries.core.viewmodel.ViewModelHost
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavOptions

@Composable
fun App() {
    val viewModelHost = remember { ViewModelHost() }
    val appConfigProvider = remember { AppConfigProvider() }
    val productRepository = remember { ProductRepository(appConfigProvider) }

    PreComposeApp {
        CompositionLocalProvider(
            LocalViewModelHost provides viewModelHost,
            LocalAppConfig provides appConfigProvider,
            LocalProductRepository provides productRepository
        ) {
            MaterialTheme {
                val appState = rememberAppState()
                val navigator = appState.navigator

                Scaffold(
                    bottomBar = {
                        if (appState.showBottomBar) {
                            BottomBar(
                                destinations = appState.topLevelDestinations,
                                currentRoute = appState.currentRoute(navigator)
                            ) {
                                navigator.navigate(
                                    route = it.route,
                                    NavOptions(
                                        launchSingleTop = true,
                                    ),
                                )
                            }
                        }
                    }
                ) {
                    AppNavHost(appState = appState)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    destinations: List<TopLevelRoute>,
    currentRoute: String?,
    navigateToTopLevelRoute: (TopLevelRoute) -> Unit,
) {
    BottomAppBar(
        backgroundColor = Color.White,
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        BottomNavigation(
            backgroundColor = Color.White,
            elevation = 0.dp
        ) {
            destinations.forEach {
                val selected = it.route == currentRoute

                BottomNavigationItem(
                    label = {
                        Text(it.label)
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) it.selectedIcon else it.unselectedIcon,
                            contentDescription = null
                        )
                    },
                    selected = selected,
                    onClick = {
                        navigateToTopLevelRoute.invoke(it)
                    }
                )
            }
        }
    }
}