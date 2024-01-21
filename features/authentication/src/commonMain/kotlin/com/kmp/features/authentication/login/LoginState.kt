package com.kmp.features.authentication.login

import com.kmp.libraries.core.state.Async

data class LoginState(
    val asyncLogin: Async<Unit> = Async.Default,
    val username: String = "",
    val password: String = "",
)
