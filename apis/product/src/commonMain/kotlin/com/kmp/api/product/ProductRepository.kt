package com.kmp.api.product

import com.kmp.api.product.model.ProductList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository {

    fun getProductList(): Flow<List<ProductList>> {
        return flow {
            emit(
                listOf(
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
            )
        }
    }
}