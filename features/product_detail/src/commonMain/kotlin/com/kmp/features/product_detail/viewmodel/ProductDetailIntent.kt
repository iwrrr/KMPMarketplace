package com.kmp.features.product_detail.viewmodel

import com.kmp.libraries.core.state.Intent

sealed class ProductDetailIntent : Intent {
    data class GetProductDetail(val productId: Int) : ProductDetailIntent()
}
