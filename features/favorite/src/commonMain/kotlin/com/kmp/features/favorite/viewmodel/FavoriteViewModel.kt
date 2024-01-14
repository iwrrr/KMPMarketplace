package com.kmp.features.favorite.viewmodel

import com.kmp.api.product.ProductRepository
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val productRepository: ProductRepository,
) : ViewModel<FavoriteState, FavoriteIntent>(FavoriteState()) {
    override fun sendIntent(intent: Intent) {
        when (intent) {
            is FavoriteIntent.GetFavoriteProducts -> {
                getFavoriteProducts()
            }
        }
    }

    private fun getFavoriteProducts() = viewModelScope.launch {
        productRepository.getFavoriteProducts()
            .stateIn(viewModelScope)
            .collectLatest {
                updateUiState {
                    copy(favoriteProducts = it)
                }
            }
    }
}