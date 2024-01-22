package com.kmp.features.product_detail.viewmodel

import com.kmp.api.product.model.product.product_detail.ProductDetail
import com.kmp.libraries.core.state.Async

data class ProductDetailState(
    val asyncProductDetail: Async<ProductDetail> = Async.Default,
    val asyncAddToCart: Async<Unit> = Async.Default,
    val isFavorite: Boolean = false
)
