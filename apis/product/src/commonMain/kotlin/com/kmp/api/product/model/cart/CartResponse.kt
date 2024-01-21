package com.kmp.api.product.model.cart


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    @SerialName("data")
    val `data`: List<DataResponse?>?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
) {
    @Serializable
    data class DataResponse(
        @SerialName("amount")
        val amount: Double?,
        @SerialName("created_at")
        val createdAt: String?,
        @SerialName("discount")
        val discount: Int?,
        @SerialName("price")
        val price: Double?,
        @SerialName("product_id")
        val productId: Int?,
        @SerialName("quantity")
        val quantity: Int?,
        @SerialName("updated_at")
        val updatedAt: String?
    )
}