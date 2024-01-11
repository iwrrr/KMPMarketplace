package com.kmp.api.product

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kmp.api.product.model.Mapper
import com.kmp.api.product.model.product.Product
import com.kmp.api.product.model.product.ProductResponse
import com.kmp.libraries.core.AppConfig
import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

class ProductPagingSources(
    appConfig: AppConfig,
    private val query: String
) : PagingSource<Int, Product>() {

    private val dataSources by lazy { ProductDataSources(appConfig) }
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        val queryPage = if (query.isNotEmpty()) {
            "$query&page=$page"
        } else {
            "?page=$page"
        }

        return try {
            val response = dataSources.getProductList(queryPage)
            val data = response.body<ProductResponse>()
                .data
                ?.filterNotNull()
                .orEmpty()
                .map(Mapper::mapItemResponseToProduct)

            when {
                response.status.isSuccess() -> {
                    val prevKey = (page - 1).takeIf { it >= 1 }
                    val nextKey = if (data.isNotEmpty()) page + 1 else null

                    LoadResult.Page(
                        data = data,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                else -> {
                    val throwable = Throwable(response.bodyAsText())
                    LoadResult.Error(throwable)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}