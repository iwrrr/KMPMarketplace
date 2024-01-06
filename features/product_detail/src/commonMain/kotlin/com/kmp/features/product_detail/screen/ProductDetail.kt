package com.kmp.features.product_detail.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProductDetail(productId: Int) {
    Scaffold {
        Column(modifier = Modifier.systemBarsPadding()) {
            Text(text = "DETAIL $productId")
        }
    }
}