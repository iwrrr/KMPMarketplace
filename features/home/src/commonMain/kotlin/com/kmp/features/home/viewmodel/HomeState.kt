package com.kmp.features.home.viewmodel

import com.kmp.api.product.model.category.Category
import com.kmp.api.product.model.product.product.Product
import com.kmp.libraries.core.state.Async

data class HomeState(
    val asyncCategoryList: Async<List<Category>> = Async.Default,
    val asyncProductList: Async<List<Product>> = Async.Default
)
