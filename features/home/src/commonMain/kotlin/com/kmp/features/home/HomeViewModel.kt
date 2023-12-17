package com.kmp.features.home

import com.kmp.api.product.ProductRepository
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: ProductRepository) :
    ViewModel<HomeState, HomeIntent>(HomeState()) {

    override fun sendIntent(intent: Intent) {
        when (intent) {
            is HomeIntent.SetName -> {
                updateName(intent.name)
            }

            is HomeIntent.GetProductList -> {
                getProductList()
            }

            is HomeIntent.ShowSnackbar -> {
                intent.coroutineScope.launch {
                    intent.snackbarState.showSnackbar(intent.name)
                }
            }
        }
    }

    fun getProductList() = viewModelScope.launch {
        productRepository.getProductList()
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(asyncProductList = it)
                }
            }
    }

    private fun updateName(name: String) = viewModelScope.launch {
        updateUiState {
            copy(name = name)
        }
    }
}