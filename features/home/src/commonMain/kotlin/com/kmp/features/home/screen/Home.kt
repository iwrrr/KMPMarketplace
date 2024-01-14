package com.kmp.features.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.api.product.LocalProductRepository
import com.kmp.api.product.model.category.Category
import com.kmp.api.product.model.product.product.Product
import com.kmp.features.home.component.CategorySection
import com.kmp.features.home.component.TopProductSection
import com.kmp.features.home.component.staggeredProductItems
import com.kmp.features.home.viewmodel.HomeViewModel
import com.kmp.libraries.core.viewmodel.rememberViewModel

@Composable
fun Home(
    navigateToProductDetail: (Product) -> Unit,
    navigateToProductList: (Category) -> Unit
) {
    val productRepository = LocalProductRepository.current
    val viewModel = rememberViewModel { HomeViewModel(productRepository) }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                contentPadding = WindowInsets
                    .systemBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                    .asPaddingValues(),
                content = {
                    Text(text = "Marketplace")
                },
                elevation = 0.dp
            )
        }
    ) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.navigationBarsPadding(),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                CategorySection(
                    state = state,
                    onCategoryClick = navigateToProductList
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                TopProductSection(
                    state = state,
                    onItemClick = navigateToProductDetail
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                TopProductSection(
                    state = state,
                    onItemClick = navigateToProductDetail
                )
            }
            staggeredProductItems(
                state = state,
                onItemClick = navigateToProductDetail
            )
        }
    }
}