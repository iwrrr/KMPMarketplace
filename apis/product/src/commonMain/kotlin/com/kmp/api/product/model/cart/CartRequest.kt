package com.kmp.api.product.model.cart

import com.kmp.libraries.core.network.model.FormData

data class CartRequest(
    override val key: String,
    override val value: String
) : FormData
