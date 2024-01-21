package com.kmp.api.authentication

import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.local.TokenDataSources
import com.kmp.libraries.core.network.NetworkDataSources
import io.ktor.client.statement.HttpResponse

class AuthenticationDataSources(
    appConfig: AppConfig,
    tokenDataSources: TokenDataSources
) : NetworkDataSources(appConfig.baseUrl, tokenDataSources) {

    suspend fun login(username: String, password: String): HttpResponse {
        val loginRequest = LoginRequest(username, password)
        return postHttpResponse("/auth/login", loginRequest)
    }

    suspend fun register(username: String, password: String): HttpResponse {
        val registerRequest = RegisterRequest(username, password)
        return postHttpResponse("/auth/register", registerRequest)
    }
}