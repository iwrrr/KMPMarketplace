package com.kmp.api.product

import com.kmp.libraries.core.network.NetworkDataSources
import io.ktor.client.statement.HttpResponse

class ProductDataSources : NetworkDataSources("https://marketfake.fly.dev/") {

    suspend fun getProductList(): HttpResponse {
        val endpoint = "product"
        return getHttpResponse(endpoint)
    }
}