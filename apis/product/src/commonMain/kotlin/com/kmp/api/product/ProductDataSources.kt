package com.kmp.api.product

import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.network.NetworkDataSources
import io.ktor.client.statement.HttpResponse

class ProductDataSources(
    appConfig: AppConfig
) : NetworkDataSources(appConfig.baseUrl) {

    suspend fun getCategoryList(): HttpResponse {
        val endpoint = "product/category"
        return getHttpResponse(endpoint)
    }

    suspend fun getProductList(query: String): HttpResponse {
        val endpoint = "product$query"
        return getHttpResponse(endpoint)
    }
}