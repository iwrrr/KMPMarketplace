package com.kmp.features.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kmp.api.product.ProductRepository
import com.kmp.features.home.component.CategorySection
import com.kmp.features.home.component.HeaderSection
import com.kmp.features.home.component.TopProductSection
import com.kmp.features.home.component.staggeredProductItems
import com.kmp.features.home.viewmodel.HomeViewModel
import com.kmp.libraries.core.LocalAppConfig
import com.kmp.libraries.core.viewmodel.rememberViewModel

@Composable
fun Home() {
    val appConfig = LocalAppConfig.current
    val productRepository = remember { ProductRepository(appConfig) }
    val homeViewModel = rememberViewModel { HomeViewModel(productRepository) }

    val homeState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            HeaderSection()
        }
    ) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.navigationBarsPadding(),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp,
            contentPadding = PaddingValues(12.dp)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) { CategorySection(homeState) }
            item(span = StaggeredGridItemSpan.FullLine) { TopProductSection(homeState) }
            item(span = StaggeredGridItemSpan.FullLine) { TopProductSection(homeState) }
            staggeredProductItems(homeState)
        }
    }
}