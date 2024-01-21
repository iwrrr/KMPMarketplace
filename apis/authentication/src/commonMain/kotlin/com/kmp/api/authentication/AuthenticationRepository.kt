package com.kmp.api.authentication

import androidx.compose.runtime.compositionLocalOf
import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.local.TokenDataSources
import com.kmp.libraries.core.repository.Repository
import com.kmp.libraries.core.state.Async
import kotlinx.coroutines.flow.Flow

class AuthenticationRepository(
    private val appConfig: AppConfig,
    private val tokenDataSources: TokenDataSources
) : Repository() {

    private val dataSources by lazy { AuthenticationDataSources(appConfig, tokenDataSources) }

    fun login(username: String, password: String): Flow<Async<Unit>> {
        return suspend {
            dataSources.login(username, password)
        }.reduce<LoginResponse, Unit> { response ->
            val responseData = response.data

            if (responseData == null) {
                Async.Failure(Throwable("Invalid data"))
            } else {
                tokenDataSources.setToken(responseData.token.orEmpty())
                Async.Success(Unit)
            }
        }
    }

    fun register(username: String, password: String): Flow<Async<Unit>> {
        return suspend {
            dataSources.register(username, password)
        }.reduce<LoginResponse, Unit> {
            Async.Success(Unit)
        }
    }
}

val LocalAuthenticationRepository =
    compositionLocalOf<AuthenticationRepository> { error("AuthenticationRepository not provided") }