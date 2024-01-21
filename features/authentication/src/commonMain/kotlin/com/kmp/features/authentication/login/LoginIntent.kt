package com.kmp.features.authentication.login

import com.kmp.libraries.core.state.Intent

sealed class LoginIntent : Intent {
    data object Login : Intent
    data class OnUsernameChanged(val username: String) : Intent
    data class OnPasswordChanged(val password: String) : Intent
}