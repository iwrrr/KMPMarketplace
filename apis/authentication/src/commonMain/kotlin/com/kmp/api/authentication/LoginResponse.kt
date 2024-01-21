package com.kmp.api.authentication


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("data")
    val `data`: DataResponse?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
) {
    @Serializable
    data class DataResponse(
        @SerialName("token")
        val token: String?
    )
}