package com.kmp.features.home

import com.kmp.api.product.ProductApi
import com.kmp.api.product.model.ProductList
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val productApi: ProductApi) : ViewModel() {

    val productList = MutableStateFlow<List<ProductList>>(emptyList())

    fun getProductList() = viewModelScope.launch {
        productApi.getProductList()
            .stateIn(this)
            .collect(productList)
    }
}