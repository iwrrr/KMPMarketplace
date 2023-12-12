package com.kmp.libraries.core.viewmodel

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel() {
    val viewModelScope: CoroutineScope

    fun cleared()
}

@Composable
expect fun <T : ViewModel> rememberViewModel(isRetain: Boolean = true, viewModel: () -> T): T