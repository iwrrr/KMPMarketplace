import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kmp.features.favorite.screen.Favorite
import com.kmp.features.home.screen.Home
import com.kmp.features.product_detail.screen.ProductDetail
import com.kmp.features.product_list.ProductList
import com.kmp.features.product_list.ProductListArgument
import com.kmp.libraries.component.utils.toData
import com.kmp.libraries.component.utils.toJson
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun AppNavHost(
    appState: AppState
) {
    val navigator = appState.navigator

    val contentPadding = if (appState.showBottomBar) 56.dp else 0.dp
    val bottomPadding by animateDpAsState(targetValue = contentPadding)

    val topLevelTransition = NavTransition(
        createTransition = fadeIn(tween(200)),
        destroyTransition = fadeOut(tween(200)),
        pauseTransition = fadeOut(tween(200)),
        resumeTransition = fadeIn(tween(200))
    )

    val defaultTransition = NavTransition(
        createTransition = enterTransition,
        destroyTransition = popExitTransition,
        pauseTransition = exitTransition,
        resumeTransition = popEnterTransition
    )

    NavHost(
        modifier = Modifier.padding(bottom = bottomPadding),
        navigator = navigator,
        navTransition = if (appState.showBottomBar) topLevelTransition else defaultTransition,
        initialRoute = Routes.Home.route
    ) {
        scene(route = Routes.Home.route) {
            Home(
                navigateToProductDetail = {
                    val argument = it.id.toString()

                    navigator.navigate(Routes.ProductDetail.route.withArgument(argument))
                },
                navigateToProductList = {
                    val argument = ProductListArgument(
                        categoryId = it.id,
                        categoryName = it.name
                    ).toJson()

                    navigator.navigate(Routes.ProductList.route.withArgument(argument))
                }
            )
        }

        scene(route = Routes.ProductList.route) {
            val argument = it.pathMap[PRODUCT_LIST_ARG] ?: "{}"
            val data = argument.toData<ProductListArgument>()

            ProductList(
                categoryName = data.categoryName,
                categoryId = data.categoryId
            )
        }

        scene(route = Routes.ProductDetail.route) {
            val productId = it.pathMap[PRODUCT_ID].orEmpty().toIntOrNull() ?: 0

            ProductDetail(
                productId = productId,
                navigateBack = { navigator.goBack() }
            )
        }

        scene(route = Routes.Favorite.route) {
            Favorite(
                navigateToProductDetail = {
                    val argument = it.id.toString()

                    navigator.navigate(Routes.ProductDetail.route.withArgument(argument))
                }
            )
        }
    }
}

private fun String.withArgument(argument: String): String {
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