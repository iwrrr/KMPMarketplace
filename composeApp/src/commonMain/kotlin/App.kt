import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.kmp.features.home.Home
import com.kmp.libraries.core.viewmodel.LocalViewModelHost
import com.kmp.libraries.core.viewmodel.ViewModelHost

@Composable
fun App() {
    val viewModelHost = remember { ViewModelHost() }

    CompositionLocalProvider(
        LocalViewModelHost provides viewModelHost
    ) {
        MaterialTheme {
            Home()
        }
    }
}