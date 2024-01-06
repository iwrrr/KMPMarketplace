package com.kmp.features.product_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProductList(categoryName: String, categoryId: Int) {
    Text(
        modifier = Modifier.statusBarsPadding().fillMaxSize(),
        text = "$categoryName $categoryId"
    )
}