package com.kmp.features.authentication.register

import com.kmp.libraries.core.state.Async

data class RegisterState(
    val asyncLogin: Async<Unit> = Async.Default,
    val username: String = "",
    val password: String = "",
)
