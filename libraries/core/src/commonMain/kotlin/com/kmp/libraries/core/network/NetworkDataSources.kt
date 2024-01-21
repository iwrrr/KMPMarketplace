package com.kmp.libraries.core.network

import com.kmp.libraries.core.local.ITokenDataSources
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

abstract class NetworkDataSources(
    private val baseUrl: String,
    private val tokenDataSources: ITokenDataSources = ITokenDataSources.Default
) {

    suspend fun getHttpResponse(endpoint: String): HttpResponse {
        val url = "$baseUrl$endpoint"
        return client.get(url) {
            headers.append(
                name = "Authorization",
                value = "Bearer ${tokenDataSources.getToken}"
            )
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun postHttpResponse(endpoint: String, body: Any): HttpResponse {
        val url = "$baseUrl$endpoint"
        return client.post(url) {
            headers.append(
                name = "Authorization",
                value = "Bearer ${tokenDataSources.getToken}"
            )
            setBody(body)
            contentType(ContentType.Application.Json)
        }
    }

    companion object {
        private val client: HttpClient by lazy {
            HttpClient {
                install(ContentNegotiation) {
                    json(
                        json = Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.BODY
                }
            }
        }
    }
}