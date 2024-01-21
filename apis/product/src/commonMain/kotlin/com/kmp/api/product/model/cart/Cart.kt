package com.kmp.api.product.model.cart

data class Cart(
    val amount: Double,
    val createdAt: String,
    val discount: Int,
    val price: Double,
    val productId: Int,
    val quantity: Int,
    val updatedAt: String
)
