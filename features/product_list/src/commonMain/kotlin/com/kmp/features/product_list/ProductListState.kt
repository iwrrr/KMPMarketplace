package com.kmp.features.product_list

import androidx.paging.PagingData
import com.kmp.api.product.model.product.Product
import com.kmp.libraries.core.state.Async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProductListState(
    val categoryName: String = "",
    val asyncProductList: Async<List<Product>> = Async.Default,
    val pagingDataProduct: Flow<PagingData<Product>> = emptyFlow()
)
