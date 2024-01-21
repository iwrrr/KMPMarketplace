package com.kmp.features.cart

import com.kmp.api.product.ProductRepository
import com.kmp.api.product.model.Mapper
import com.kmp.libraries.core.state.Async
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val productRepository: ProductRepository
) : ViewModel<CartState, CartIntent>(CartState()) {
    override fun sendIntent(intent: Intent) {
        when (intent) {
            is CartIntent.GetCartList -> {
                getCartList()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCartList() = viewModelScope.launch {
        productRepository.getCartList()
            .flatMapMerge {
                when (it) {
                    Async.Loading -> {
                        flowOf(Async.Loading)
                    }

                    is Async.Success -> {
                        val data = it.data.map { cart ->
                            val result = when (val asyncProductDetail =
                                productRepository.getProductDetail(cart.productId).last()) {
                                is Async.Success -> {
                                    Mapper.mapCartToCartProduct(cart, asyncProductDetail.data)
                                }

                                else -> null
                            }
                            return@map result
                        }
                        flowOf(Async.Success(data))
                    }

                    is Async.Failure -> {
                        flowOf(Async.Failure(it.throwable))
                    }

                    else -> emptyFlow()
                }
            }
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(asyncCartProductList = it)
                }
            }

//        productRepository.getCartList()
//            .stateIn(this)
//            .collectLatest {
//                updateUiState {
//                    copy(asyncCartList = it)
//                }
//            }
    }
}