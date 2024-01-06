import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.kmp.features.home.screen.Home
import com.kmp.features.product_detail.screen.ProductDetail
import com.kmp.libraries.core.LocalAppConfig
import com.kmp.libraries.core.viewmodel.LocalViewModelHost
import com.kmp.libraries.core.viewmodel.ViewModelHost
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun App() {
    val viewModelHost = remember { ViewModelHost() }
    val appConfigProvider = remember { AppConfigProvider() }

    PreComposeApp {
        CompositionLocalProvider(
            LocalViewModelHost provides viewModelHost,
            LocalAppConfig provides appConfigProvider
        ) {
            MaterialTheme {
                val navigator = rememberNavigator()
                NavHost(
                    navigator = navigator,
                    navTransition = NavTransition(
                        createTransition = enterTransition,
                        destroyTransition = popExitTransition,
                        pauseTransition = exitTransition,
                        resumeTransition = popEnterTransition
                    ),
                    initialRoute = "/home"
                ) {
                    scene(route = "/home") {
                        Home(navigateToProductDetail = { navigator.navigate("/product/${it.id}") })
                    }
                    scene(route = "/product/{id}") {
                        val productId = it.pathMap["id"].orEmpty().toIntOrNull() ?: 0
                        ProductDetail(productId = productId)
                    }
                }
            }
        }
    }
}

val enterTransition: EnterTransition by lazy {
    fadeIn(animationSpec = tween(200)) + slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(200)
    )
}

val exitTransition: ExitTransition by lazy {
    fadeOut(animationSpec = tween(200)) + slideOutHorizontally(
        targetOffsetX = { -300 },
        animationSpec = tween(200)
    )
}

val popEnterTransition: EnterTransition by lazy {
    fadeIn(animationSpec = tween(200)) + slideInHorizontally(
        initialOffsetX = { -300 },
        animationSpec = tween(200)
    )
}

val popExitTransition: ExitTransition by lazy {
    fadeOut(animationSpec = tween(200)) + slideOutHorizontally(
        targetOffsetX = { 300 },
        animationSpec = tween(200)
    )
}