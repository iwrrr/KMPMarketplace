package com.kmp.api.product.model.product.product_detail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponse(
    @SerialName("data")
    val `data`: DataResponse?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
) {
    @Serializable
    data class DataResponse(
        @SerialName("category")
        val category: CategoryResponse?,
        @SerialName("description")
        val description: String?,
        @SerialName("discount")
        val discount: Int?,
        @SerialName("id")
        val id: Int?,
        @SerialName("images")
        val images: List<String>?,
        @SerialName("name")
        val name: String?,
        @SerialName("price")
        val price: Double?,
        @SerialName("rating")
        val rating: Double?,
        @SerialName("user_review")
        val userReview: List<UserReviewResponse?>?
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

        @Serializable
        data class UserReviewResponse(
            @SerialName("review")
            val review: String?,
            @SerialName("user")
            val user: String?
        )
    }
}