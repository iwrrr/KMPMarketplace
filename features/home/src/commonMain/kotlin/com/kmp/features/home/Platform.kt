package com.kmp.features.home

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform