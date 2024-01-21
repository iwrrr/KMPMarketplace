package com.kmp.api.product

import androidx.compose.runtime.compositionLocalOf
import com.kmp.api.product.model.Mapper
import com.kmp.api.product.model.cart.Cart
import com.kmp.api.product.model.cart.CartResponse
import com.kmp.api.product.model.category.Category
import com.kmp.api.product.model.category.CategoryResponse
import com.kmp.api.product.model.product.product.Product
import com.kmp.api.product.model.product.product.ProductResponse
import com.kmp.api.product.model.product.product_detail.ProductDetail
import com.kmp.api.product.model.product.product_detail.ProductDetailResponse
import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.local.TokenDataSources
import com.kmp.libraries.core.network.UnauthorizedException
import com.kmp.libraries.core.repository.Repository
import com.kmp.libraries.core.state.Async
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository(
    private val appConfig: AppConfig,
    private val tokenDataSources: TokenDataSources
) : Repository() {

    private val productDataSources by lazy { ProductDataSources(appConfig) }
    private val favoriteDataSources by lazy { ProductFavoriteDataSources() }
    private val cartDataSources by lazy { CartDataSources(appConfig, tokenDataSources) }

    fun getCategoryList(): Flow<Async<List<Category>>> {
        return suspend {
            productDataSources.getCategoryList()
        }.reduce<CategoryResponse, List<Category>> { response ->
            val responseData = response.data

            if (responseData.isNullOrEmpty()) {
                val throwable = Throwable("Category is empty")
                Async.Failure(throwable)
            } else {
                val moreCategory = Category(id = -1, name = "More", description = "")
                val data = Mapper.mapResponseToCategoryList(response).take(7).toMutableList()
                data.add(moreCategory)
                Async.Success(data)
            }
        }
    }

    fun getProductList(query: String): Flow<Async<List<Product>>> {
        return suspend {
            productDataSources.getProductList(query)
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

    fun getProductDetail(productId: Int): Flow<Async<ProductDetail>> {
        return suspend {
            productDataSources.getProductDetail(productId)
        }.reduce<ProductDetailResponse, ProductDetail> { response ->
            val responseData = response.data

            if (responseData == null) {
                val throwable = Throwable("Product not found")
                Async.Failure(throwable)
            } else {
                val data = Mapper.mapResponseToProductDetail(response)
                Async.Success(data)
            }
        }
    }

    suspend fun getFavoriteProducts(): Flow<List<Product>> {
        return favoriteDataSources.getFavoriteProducts()
    }

    suspend fun checkIsFavorite(productId: Int): Flow<Boolean> {
        return favoriteDataSources.checkIsFavorite(productId)
    }

    suspend fun insertFavorite(productDetail: ProductDetail) {
        return favoriteDataSources.insertProduct(productDetail)
    }

    suspend fun deleteFavorite(productId: Int) {
        return favoriteDataSources.removeProduct(productId)
    }

    fun getCartList(): Flow<Async<List<Cart>>> {
        return suspend {
            cartDataSources.getCartList()
        }.reduce<CartResponse, List<Cart>> { response ->
            val responseData = response.data

            if (responseData.isNullOrEmpty()) {
                val throwable = Throwable("Cart is empty")
                Async.Failure(throwable)
            } else {
                val data = Mapper.mapResponseToCartList(response)
                Async.Success(data)
            }
        }
    }

    fun getCartListTest(): Flow<List<Cart>> = flow {
        val httpResponse = cartDataSources.getCartList()

        when {
            httpResponse.status.isSuccess() -> {
                val response = httpResponse.body<CartResponse>()
                val data = Mapper.mapResponseToCartList(response)
                emit(data)
            }

            httpResponse.status == HttpStatusCode.Unauthorized -> {
                val throwable = UnauthorizedException()
                throw throwable
            }

            else -> {
                val throwable = Throwable(httpResponse.bodyAsText())
                throw throwable
            }
        }
    }
}

val LocalProductRepository =
    compositionLocalOf<ProductRepository> { error("ProductRepository not provided") }