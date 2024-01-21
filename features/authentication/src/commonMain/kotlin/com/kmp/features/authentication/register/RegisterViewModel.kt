package com.kmp.features.authentication.register

import com.kmp.api.authentication.AuthenticationRepository
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel<RegisterState, RegisterIntent>(RegisterState()) {
    override fun sendIntent(intent: Intent) {
        when (intent) {
            is RegisterIntent.Register -> {
                register()
            }

            is RegisterIntent.OnUsernameChanged -> {
                onUsernameChanged(intent.username)
            }

            is RegisterIntent.OnPasswordChanged -> {
                onPasswordChanged(intent.password)
            }
        }
    }

    private fun register() = viewModelScope.launch {
        val username = uiState.value.username
        val password = uiState.value.password

        authenticationRepository.register(username, password)
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