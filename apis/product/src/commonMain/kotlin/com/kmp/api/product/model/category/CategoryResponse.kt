package com.kmp.api.product.model.category


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("data")
    val `data`: List<DataResponse?>?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
) {
    @Serializable
    data class DataResponse(
        @SerialName("description")
        val description: String?,
        @SerialName("id")
        val id: Int?,
        @SerialName("name")
        val name: String?
    )
}