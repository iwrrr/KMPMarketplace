package com.kmp.api.product

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform