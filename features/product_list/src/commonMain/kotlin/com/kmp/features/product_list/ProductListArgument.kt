package com.kmp.features.product_list

import kotlinx.serialization.Serializable

@Serializable
class ProductListArgument(
    val categoryId: Int,
    val categoryName: String
)