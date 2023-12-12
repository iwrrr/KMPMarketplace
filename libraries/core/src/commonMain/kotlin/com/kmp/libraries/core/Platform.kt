package com.kmp.libraries.core

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform