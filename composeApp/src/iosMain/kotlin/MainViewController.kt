import androidx.compose.ui.window.ComposeUIViewController
import com.kmp.libraries.component.ui.Action
import com.kmp.libraries.component.ui.store

fun MainViewController() = ComposeUIViewController { App() }

fun onBackGesture() {
    store.send(Action.OnBackPressed)
}