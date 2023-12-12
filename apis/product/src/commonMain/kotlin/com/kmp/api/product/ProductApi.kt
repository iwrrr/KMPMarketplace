package com.kmp.api.product

import com.kmp.api.product.model.ProductList

class ProductApi {

    fun getProducts(): List<ProductList> {
        return listOf(
            ProductList(
                id = 2847,
                name = "Meja",
                price = 78.832
            ),
            ProductList(
                id = 2847,
                name = "Kursi",
                price = 78.832
            ),
        )
    }
}