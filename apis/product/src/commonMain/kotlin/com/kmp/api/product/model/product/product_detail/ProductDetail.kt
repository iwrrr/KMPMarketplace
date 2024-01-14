package com.kmp.api.product.model.product.product_detail

import com.kmp.api.product.model.category.Category

data class ProductDetail(
    val id: Int,
    val category: Category,
    val description: String,
    val discount: Int,
    val images: List<String>,
    val name: String,
    val price: Double,
    val rating: Double,
    val userReview: List<UserReview>
)