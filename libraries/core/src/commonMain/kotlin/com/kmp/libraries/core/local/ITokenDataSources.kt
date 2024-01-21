package com.kmp.libraries.core.local

import androidx.compose.runtime.compositionLocalOf

interface ITokenDataSources {
    val getToken: String

    companion object {
        val Default = object : ITokenDataSources {
            override val getToken: String
                get() = ""
        }
    }
}

class TokenDataSources(
    private val valueDataSources: ValueDataSources
) : ITokenDataSources {
    override val getToken: String
        get() = valueDataSources.getString("token")

    fun setToken(token: String) {
        valueDataSources.setString("token", token)
    }
}

val LocalTokenDataSources =
    compositionLocalOf<TokenDataSources> { error("TokenDataSources not provided") }