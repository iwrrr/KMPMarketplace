package com.kmp.features.home

import com.kmp.api.product.model.ProductList
import com.kmp.libraries.core.state.Async

data class HomeState(
    val name: String = "",
    val asyncProductList: Async<List<ProductList>> = Async.Default
)
