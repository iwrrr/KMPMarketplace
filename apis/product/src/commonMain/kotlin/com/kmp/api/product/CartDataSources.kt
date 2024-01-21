package com.kmp.api.product

import com.kmp.api.product.model.cart.CartRequest
import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.local.TokenDataSources
import com.kmp.libraries.core.network.NetworkDataSources
import io.ktor.client.statement.HttpResponse

class CartDataSources(
    appConfig: AppConfig,
    tokenDataSources: TokenDataSources
) : NetworkDataSources(appConfig.baseUrl, tokenDataSources) {

    suspend fun getCartList(): HttpResponse {
        val endpoint = "cart"
        return getHttpResponse(endpoint)
    }

    suspend fun addToCart(
        productId: String,
        qty: String
    ): HttpResponse {
        val endpoint = "cart"
        val cartRequest = CartRequest(key = productId, value = qty)
        return postHttpResponse(endpoint, cartRequest)
    }
}