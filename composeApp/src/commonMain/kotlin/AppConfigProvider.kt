import com.kmp.libraries.core.AppConfig
import com.kmp.marketplace.BuildKonfig

class AppConfigProvider : AppConfig {
    override val baseUrl: String
        get() = BuildKonfig.BASE_URL
}