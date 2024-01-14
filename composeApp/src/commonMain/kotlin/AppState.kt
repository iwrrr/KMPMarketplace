import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun rememberAppState(
    navigator: Navigator = rememberNavigator(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState {
    return remember(navigator, coroutineScope) { AppState(navigator) }
}

@Stable
class AppState(
    val navigator: Navigator
) {
    val topLevelDestinations: List<TopLevelRoute> = TopLevelRoute.entries
    private val topLevelRoutes = topLevelDestinations.map { it.route }

    val showBottomBar: Boolean
        @Composable get() = navigator.currentEntry.collectAsState(null).value?.route?.route in topLevelRoutes

    @Composable
    fun currentRoute(navigator: Navigator): String? {
        return navigator.currentEntry.collectAsState(null).value?.route?.route
    }
}