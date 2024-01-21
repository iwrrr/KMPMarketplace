package com.kmp.features.authentication.register

import com.kmp.libraries.core.state.Intent

sealed class RegisterIntent : Intent {
    data object Register : Intent
    data class OnUsernameChanged(val username: String) : Intent
    data class OnPasswordChanged(val password: String) : Intent
}