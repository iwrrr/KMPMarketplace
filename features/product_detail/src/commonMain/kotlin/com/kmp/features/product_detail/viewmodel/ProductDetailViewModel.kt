package com.kmp.features.product_detail.viewmodel

import com.kmp.api.product.ProductRepository
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository
) : ViewModel<ProductDetailState, ProductDetailIntent>(ProductDetailState()) {
    override fun sendIntent(intent: Intent) {
        when (intent) {
            is ProductDetailIntent.GetProductDetail -> {
                getProductDetail(intent.productId)
            }
        }
    }

    private fun getProductDetail(productId: Int) = viewModelScope.launch {
        productRepository.getProductDetail(productId)
            .stateIn(viewModelScope)
            .collectLatest {
                updateUiState {
                    copy(asyncProductDetail = it)
                }
            }
    }
}