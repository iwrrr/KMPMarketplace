package com.kmp.features.product_detail.viewmodel

import com.kmp.api.product.model.product.product_detail.ProductDetail
import com.kmp.libraries.core.state.Intent

sealed class ProductDetailIntent : Intent {
    data class GetProductDetail(val productId: Int) : ProductDetailIntent()
    data class ToggleFavorite(val productDetail: ProductDetail) : ProductDetailIntent()
}
