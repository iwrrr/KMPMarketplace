package com.kmp.features.authentication.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.kmp.api.authentication.LocalAuthenticationRepository
import com.kmp.libraries.core.state.Async
import com.kmp.libraries.core.viewmodel.rememberViewModel

@Composable
fun Login(
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
) {
    val authenticationRepository = LocalAuthenticationRepository.current
    val viewModel = rememberViewModel { LoginViewModel(authenticationRepository) }

    val state by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.username,
                    onValueChange = {
                        viewModel.sendIntent(LoginIntent.OnUsernameChanged(it))
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.password,
                    onValueChange = {
                        viewModel.sendIntent(LoginIntent.OnPasswordChanged(it))
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp
                    ),
                    onClick = { viewModel.sendIntent(LoginIntent.Login) }
                ) {
                    Text(text = "Login")
                }
                ClickableText(
                    text = AnnotatedString(text = "Register"),
                    onClick = { navigateToRegister.invoke() }
                )
            }

            when (val async = state.asyncLogin) {
                Async.Loading -> {
                    CircularProgressIndicator()
                }

                is Async.Success -> {
                    navigateToHome.invoke()
                }

                is Async.Failure -> {
                    Text(text = async.throwable.message.orEmpty())
                }

                else -> {}
            }
        }
    }
}