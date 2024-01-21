package com.kmp.api.product.model.cart

import com.kmp.api.product.model.product.product_detail.ProductDetail

data class CartProduct(
    val amount: Double,
    val createdAt: String,
    val discount: Int,
    val price: Double,
    val productId: Int,
    val quantity: Int,
    val updatedAt: String,
    val productDetail: ProductDetail
)
