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
import com.kmp.features.product_list.ProductList
import com.kmp.features.product_list.ProductListArgument
import com.kmp.libraries.component.utils.toData
import com.kmp.libraries.component.utils.toJson
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
                        Home(
                            navigateToProductDetail = { navigator.navigate("/product/${it.id}") },
                            navigateToProductList = {
                                val argument = ProductListArgument(
                                    categoryId = it.id,
                                    categoryName = it.name
                                ).toJson()

                                navigator.navigate("/list/$argument")
                            },
                        )
                    }
                    scene(route = "/product/{id}") {
                        val productId = it.pathMap["id"].orEmpty().toIntOrNull() ?: 0
                        ProductDetail(productId = productId)
                    }
                    scene(route = "/list/{argument}") {
                        val argumentJson = it.pathMap["argument"] ?: "{}"
                        val argument = argumentJson.toData<ProductListArgument>()
                        ProductList(
                            categoryName = argument.categoryName,
                            categoryId = argument.categoryId
                        )
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