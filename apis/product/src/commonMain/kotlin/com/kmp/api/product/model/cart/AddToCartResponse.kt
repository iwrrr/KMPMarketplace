package com.kmp.api.product.model.cart


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartResponse(
    @SerialName("data")
    val `data`: String?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
)