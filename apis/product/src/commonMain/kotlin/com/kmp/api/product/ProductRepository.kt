package com.kmp.api.product

import com.kmp.api.product.model.Mapper
import com.kmp.api.product.model.category.Category
import com.kmp.api.product.model.category.CategoryResponse
import com.kmp.api.product.model.product.Product
import com.kmp.api.product.model.product.ProductResponse
import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.repository.Repository
import com.kmp.libraries.core.state.Async
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val appConfig: AppConfig
) : Repository() {

    private val dataSources by lazy { ProductDataSources(appConfig) }

    fun getCategoryList(): Flow<Async<List<Category>>> {
        return suspend {
            dataSources.getCategoryList()
        }.reduce<CategoryResponse, List<Category>> { response ->
            val responseData = response.data

            if (responseData.isNullOrEmpty()) {
                val throwable = Throwable("Category is empty")
                Async.Failure(throwable)
            } else {
                val moreCategory = Category(id = -1, name = "More", description = "")
                val data = Mapper.mapResponseToCategoryList(response).take(5).toMutableList()
                data.add(moreCategory)
                Async.Success(data)
            }
        }
    }

    fun getProductList(): Flow<Async<List<Product>>> {
        return suspend {
            dataSources.getProductList()
        }.reduce<ProductResponse, List<Product>> { response ->
            val responseData = response.data

            if (responseData.isNullOrEmpty()) {
                val throwable = Throwable("Product is empty")
                Async.Failure(throwable)
            } else {
                val data = Mapper.mapResponseToProductList(response)
                Async.Success(data)
            }
        }
    }
}