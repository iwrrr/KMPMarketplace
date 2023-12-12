package com.kmp.features.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kmp.api.product.ProductApi
import com.kmp.api.product.model.ProductList

@Composable
fun Home() {
    val productApi = remember { ProductApi() }
    val productList = remember { productApi.getProducts() }

    LazyColumn() {
        items(productList) {
            ProductListItem(it)
        }
    }
}

@Composable
fun ProductListItem(productList: ProductList) {
    Text(text = productList.name)
}
