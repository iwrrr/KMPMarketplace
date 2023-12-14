package com.kmp.api.product.model

data class ProductList(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String,
    val discount: Int,
)