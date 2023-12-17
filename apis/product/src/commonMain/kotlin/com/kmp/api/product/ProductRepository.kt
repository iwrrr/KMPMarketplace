package com.kmp.api.product

import com.kmp.api.product.model.Mapper
import com.kmp.api.product.model.ProductList
import com.kmp.api.product.model.ProductListResponse
import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.repository.Repository
import com.kmp.libraries.core.state.Async
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val appConfig: AppConfig
) : Repository() {

    private val dataSources by lazy { ProductDataSources(appConfig) }

    fun getProductList(): Flow<Async<List<ProductList>>> {

        return suspend {
            dataSources.getProductList()
        }.reduce<ProductListResponse, List<ProductList>> { response ->
            val responseData = response.data

            if (responseData.isNullOrEmpty()) {
                val throwable = Throwable("Product is empty")
                Async.Failure(throwable)
            } else {
                val data = Mapper.mapResponseToList(response)
                Async.Success(data)
            }
        }
    }
}