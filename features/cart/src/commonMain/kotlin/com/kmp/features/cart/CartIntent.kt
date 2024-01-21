package com.kmp.features.cart

import com.kmp.libraries.core.state.Intent

sealed class CartIntent : Intent {
    data object GetCartList : Intent
}