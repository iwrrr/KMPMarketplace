package com.kmp.features.product_detail.viewmodel

import com.kmp.api.product.ProductRepository
import com.kmp.api.product.model.product.product_detail.ProductDetail
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
                checkIsFavorite(intent.productId)
            }

            is ProductDetailIntent.ToggleFavorite -> {
                toggleFavorite(intent.productDetail)
            }

            is ProductDetailIntent.AddToCart -> {
                addToCart(intent.productId, intent.qty)
            }
        }
    }

    private fun getProductDetail(productId: Int) = viewModelScope.launch {
        productRepository.getProductDetail(productId)
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(asyncProductDetail = it)
                }
            }
    }

    private fun checkIsFavorite(productId: Int) = viewModelScope.launch {
        productRepository.checkIsFavorite(productId)
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(isFavorite = it)
                }
            }
    }

    private fun toggleFavorite(productDetail: ProductDetail) = viewModelScope.launch {
        if (uiState.value.isFavorite) {
            productRepository.deleteFavorite(productDetail.id)
        } else {
            productRepository.insertFavorite(productDetail)
        }
    }

    private fun addToCart(productId: Int, qty: Int) = viewModelScope.launch {
        productRepository.addToCart(
            productId = productId.toString(),
            qty = qty.toString()
        )
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(asyncAddToCart = it)
                }
            }
    }
}