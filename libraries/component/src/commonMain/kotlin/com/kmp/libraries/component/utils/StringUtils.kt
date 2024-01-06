package com.kmp.libraries.component.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T.toJson(): String {
    return Json.encodeToString(this)
}

inline fun <reified T> String.toData(): T {
    return Json.decodeFromString(this)
}