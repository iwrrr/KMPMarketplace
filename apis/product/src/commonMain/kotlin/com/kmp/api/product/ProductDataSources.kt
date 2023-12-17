package com.kmp.api.product

import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.network.NetworkDataSources
import io.ktor.client.statement.HttpResponse

class ProductDataSources(
    appConfig: AppConfig
) : NetworkDataSources(appConfig.baseUrl) {

    suspend fun getProductList(): HttpResponse {
        val endpoint = "product"
        return getHttpResponse(endpoint)
    }
}