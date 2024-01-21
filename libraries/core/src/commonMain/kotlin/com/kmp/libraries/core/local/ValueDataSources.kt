package com.kmp.libraries.core.local

import androidx.compose.runtime.compositionLocalOf
import com.russhwolf.settings.Settings

class ValueDataSources {
    private val settings = Settings()

    fun getString(key: String): String {
        return settings.getString(key, "")
    }

    fun setString(key: String, value: String) {
        settings.putString(key, value)
    }
}

val LocalValueDataSources =
    compositionLocalOf<ValueDataSources> { error("ValueDataSources not provided") }