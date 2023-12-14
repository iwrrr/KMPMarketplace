package com.kmp.api.product

import com.kmp.api.product.model.Mapper
import com.kmp.api.product.model.ProductList
import com.kmp.api.product.model.ProductListResponse
import io.ktor.client.call.body
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository {

    private val dataSources by lazy { ProductDataSources() }

    suspend fun getProductList(): Flow<List<ProductList>> {
        val data = dataSources
            .getProductList()
            .body<ProductListResponse>()
            .let { Mapper.mapResponseToList(it) }

        return flow {
            emit(data)
        }
    }
}