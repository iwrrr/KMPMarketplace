package com.kmp.features.favorite.viewmodel

import com.kmp.api.product.model.product.product.Product

data class FavoriteState(
    val favoriteProducts: List<Product> = emptyList()
)
