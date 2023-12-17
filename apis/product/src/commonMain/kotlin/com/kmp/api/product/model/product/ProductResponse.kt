package com.kmp.api.product.model.product


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("data")
    val `data`: List<DataResponse?>?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
) {
    @Serializable
    data class DataResponse(
        @SerialName("category")
        val category: CategoryResponse?,
        @SerialName("discount")
        val discount: Int?,
        @SerialName("id")
        val id: Int?,
        @SerialName("images")
        val images: String?,
        @SerialName("name")
        val name: String?,
        @SerialName("price")
        val price: Double?,
        @SerialName("rating")
        val rating: Double?,
        @SerialName("sort_description")
        val sortDescription: String?
    ) {
        @Serializable
        data class CategoryResponse(
            @SerialName("description")
            val description: String?,
            @SerialName("id")
            val id: Int?,
            @SerialName("name")
            val name: String?
        )
    }
}