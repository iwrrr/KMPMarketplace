
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.kmp.api.authentication.AuthenticationRepository
import com.kmp.api.authentication.LocalAuthenticationRepository
import com.kmp.api.product.LocalProductRepository
import com.kmp.api.product.ProductRepository
import com.kmp.libraries.core.LocalAppConfig
import com.kmp.libraries.core.local.LocalTokenDataSources
import com.kmp.libraries.core.local.LocalValueDataSources
import com.kmp.libraries.core.local.TokenDataSources
import com.kmp.libraries.core.local.ValueDataSources
import com.kmp.libraries.core.viewmodel.LocalViewModelHost
import com.kmp.libraries.core.viewmodel.ViewModelHost
import moe.tlaster.precompose.PreComposeApp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
    val viewModelHost = remember { ViewModelHost() }
    val appConfigProvider = remember { AppConfigProvider() }
    val tabNavigator = remember { TabNavigator() }

    val valueDataSources = remember { ValueDataSources() }
    val tokenDataSources = remember { TokenDataSources(valueDataSources) }

    val authenticationRepository =
        remember { AuthenticationRepository(appConfigProvider, tokenDataSources) }
    val productRepository = remember { ProductRepository(appConfigProvider, tokenDataSources) }

    PreComposeApp {
        CompositionLocalProvider(
            LocalViewModelHost provides viewModelHost,
            LocalAppConfig provides appConfigProvider,
            LocalTabNavigator provides tabNavigator,
            LocalValueDataSources provides valueDataSources,
            LocalTokenDataSources provides tokenDataSources,
            LocalAuthenticationRepository provides authenticationRepository,
            LocalProductRepository provides productRepository
        ) {
            MaterialTheme {
                val appState = rememberAppState()

                AppNavHost(appState = appState)
            }
        }
    }
}
