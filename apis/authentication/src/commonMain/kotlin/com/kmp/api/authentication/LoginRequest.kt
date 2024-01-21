package com.kmp.api.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("name")
    val name: String?,
    @SerialName("password")
    val password: String?
)