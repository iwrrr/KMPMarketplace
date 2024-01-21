
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kmp.features.authentication.login.Login
import com.kmp.features.authentication.register.Register
import com.kmp.features.cart.Cart
import com.kmp.features.product_detail.screen.ProductDetail
import com.kmp.features.product_list.ProductList
import com.kmp.features.product_list.ProductListArgument
import com.kmp.libraries.component.utils.toData
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.transition.NavTransition

@Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@Composable
fun AppNavHost(
    appState: AppState,
) {
    val navigator = appState.navigator

    val defaultTransition = NavTransition(
        createTransition = enterTransition,
        destroyTransition = popExitTransition,
        pauseTransition = exitTransition,
        resumeTransition = popEnterTransition
    )

    NavHost(
        navigator = navigator,
        navTransition = defaultTransition,
        swipeProperties = remember { SwipeProperties() },
        initialRoute = Routes.Main.route
    ) {
        /* ============ TOP LEVEL ROUTES ============ */

        scene(route = Routes.Main.route) {
            MainScreen(appState)
        }

        /* ============ END TOP LEVEL ROUTES ============*/

        scene(route = Routes.Login.route) {
            Login(
                navigateToHome = {
                    navigator.navigate(
                        Routes.Home.route,
                        NavOptions(
                            launchSingleTop = true,
                            popUpTo = PopUpTo.First(true)
                        )
                    )
                },
                navigateToRegister = {
                    navigator.navigate(Routes.Register.route)
                }
            )
        }

        scene(route = Routes.Register.route) {
            Register(
                navigateBack = navigator::popBackStack,
                navigateToHome = {
                    navigator.navigate(
                        Routes.Home.route,
                        NavOptions(
                            launchSingleTop = true,
                            popUpTo = PopUpTo.First(true)
                        )
                    )
                }
            )
        }

        scene(route = Routes.Cart.route) {
            Cart(
                navigateBack = navigator::popBackStack,
                navigateToLogin = {
                    navigator.navigate(Routes.Login.route)
                }
            )
        }

        scene(route = Routes.ProductList.route) {
            val argument = it.pathMap[PRODUCT_LIST_ARG] ?: "{}"
            val data = argument.toData<ProductListArgument>()

            ProductList(
                categoryName = data.categoryName,
                categoryId = data.categoryId,
                navigateToProductDetail = { product ->
                    val productId = product.id.toString()

                    navigator.navigate(Routes.ProductDetail.route.withArgument(productId))
                }
            )
        }

        scene(route = Routes.ProductDetail.route) {
            val productId = it.pathMap[PRODUCT_ID].orEmpty().toIntOrNull() ?: 0

            ProductDetail(
                productId = productId,
                navigateBack = { navigator.goBack() }
            )
        }
    }
}

fun String.withArgument(argument: String): String {
    val regex = "\\{(.+?)\\}".toRegex()
    return regex.replace(this, argument)
}

private val enterTransition: EnterTransition by lazy {
    fadeIn(animationSpec = tween(200)) + slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(200)
    )
}

private val exitTransition: ExitTransition by lazy {
    fadeOut(animationSpec = tween(200)) + slideOutHorizontally(
        targetOffsetX = { -300 },
        animationSpec = tween(200)
    )
}

private val popEnterTransition: EnterTransition by lazy {
    fadeIn(animationSpec = tween(200)) + slideInHorizontally(
        initialOffsetX = { -300 },
        animationSpec = tween(200)
    )
}

private val popExitTransition: ExitTransition by lazy {
    fadeOut(animationSpec = tween(200)) + slideOutHorizontally(
        targetOffsetX = { 300 },
        animationSpec = tween(200)
    )
}