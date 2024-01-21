package com.kmp.features.cart

import com.kmp.api.product.model.cart.CartProduct
import com.kmp.libraries.core.state.Async

data class CartState(
    val asyncCartProductList: Async<List<CartProduct?>> = Async.Default
)
