package com.kmp.features.home

import com.kmp.api.product.ProductRepository
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: ProductRepository) :
    ViewModel<HomeState, HomeIntent>(HomeState()) {

    init {
        sendIntent(
            HomeIntent.GetCategoryList
        )
        sendIntent(
            HomeIntent.GetProductList
        )
    }

    override fun sendIntent(intent: Intent) {
        when (intent) {
            is HomeIntent.GetCategoryList -> {
                getCategoryList()
            }

            is HomeIntent.GetProductList -> {
                getProductList()
            }
        }
    }

    private fun getCategoryList() = viewModelScope.launch {
        productRepository.getCategoryList()
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(asyncCategoryList = it)
                }
            }
    }

    private fun getProductList() = viewModelScope.launch {
        productRepository.getProductList()
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(asyncProductList = it)
                }
            }
    }
}