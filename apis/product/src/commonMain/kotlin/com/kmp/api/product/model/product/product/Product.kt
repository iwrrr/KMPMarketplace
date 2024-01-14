package com.kmp.api.product.model.product.product

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String,
    val discount: Int,
    val rating: Double,
)