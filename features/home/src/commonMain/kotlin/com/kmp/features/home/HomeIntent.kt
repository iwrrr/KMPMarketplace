package com.kmp.features.home

import androidx.compose.material.SnackbarHostState
import com.kmp.libraries.core.state.Intent
import kotlinx.coroutines.CoroutineScope

sealed class HomeIntent : Intent {
    data class SetName(val name: String) : HomeIntent()
    data object GetProductList : HomeIntent()
    data class ShowSnackbar(
        val name: String,
        val snackbarState: SnackbarHostState,
        val coroutineScope: CoroutineScope
    ) : HomeIntent()
}