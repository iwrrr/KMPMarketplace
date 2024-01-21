package com.kmp.features.authentication.login

import com.kmp.api.authentication.AuthenticationRepository
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel<LoginState, LoginIntent>(LoginState()) {
    override fun sendIntent(intent: Intent) {
        when (intent) {
            is LoginIntent.Login -> {
                login()
            }

            is LoginIntent.OnUsernameChanged -> {
                onUsernameChanged(intent.username)
            }

            is LoginIntent.OnPasswordChanged -> {
                onPasswordChanged(intent.password)
            }
        }
    }

    private fun login() = viewModelScope.launch {
        val username = uiState.value.username
        val password = uiState.value.password

        authenticationRepository.login(username, password)
            .stateIn(this)
            .collectLatest {
                updateUiState {
                    copy(asyncLogin = it)
                }
            }
    }

    private fun onUsernameChanged(username: String) {
        updateUiState {
            copy(username = username)
        }
    }

    private fun onPasswordChanged(password: String) {
        updateUiState {
            copy(password = password)
        }
    }
}