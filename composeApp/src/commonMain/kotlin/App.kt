import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.kmp.features.home.Home
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun App() {
    MaterialTheme {
        Home()
    }
}