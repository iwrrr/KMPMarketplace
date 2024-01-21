package com.kmp.features.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.kmp.libraries.component.ui.CustomTextField
import com.kmp.libraries.component.utils.bounceClickable
import com.kmp.libraries.core.viewmodel.rememberViewModel

@Composable
fun Home(
    navigateToProductDetail: (Product) -> Unit,
    navigateToProductList: (Category) -> Unit,
    navigateToCart: () -> Unit,
) {
    val productRepository = LocalProductRepository.current
    val viewModel = rememberViewModel { HomeViewModel(productRepository) }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            var value by remember { mutableStateOf("") }
            TopAppBar(
                backgroundColor = Color.White,
                contentPadding = WindowInsets
                    .systemBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                    .asPaddingValues(),
                content = {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomTextField(
                            modifier = Modifier
                                .weight(5f)
                                .bounceClickable(scaleDown = 1f) {},
                            value = value,
                            onValueChange = {
                                value = it
                            },
                            enabled = false,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = null
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.LightGray.copy(alpha = 0.3f),
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent
                            )
                        )
                        BadgedBox(
                            modifier = Modifier
                                .bounceClickable(onClick = navigateToCart)
                                .padding(start = 16.dp, end = 24.dp),
                            badge = {
                                Badge {
                                    Text(text = "1")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = null
                            )
                        }
                    }
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
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 24.dp
            )
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