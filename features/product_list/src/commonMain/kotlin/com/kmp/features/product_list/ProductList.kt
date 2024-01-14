package com.kmp.features.product_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import com.kmp.api.product.model.product.product.Product
import com.kmp.libraries.component.utils.bounceClickable
import com.kmp.libraries.component.utils.toRupiah
import com.kmp.libraries.core.LocalAppConfig
import com.kmp.libraries.core.viewmodel.rememberViewModel
import com.seiko.imageloader.rememberImagePainter

@Composable
fun ProductList(
    categoryName: String,
    categoryId: Int,
    navigateToProductDetail: (Product) -> Unit
) {
    val appConfig = LocalAppConfig.current
    val viewModel = rememberViewModel { ProductListViewModel(appConfig) }

    val state by viewModel.uiState.collectAsState()
    val pagingData = state.pagingDataProduct.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ProductListIntent.GetProductList(categoryId))
        viewModel.sendIntent(ProductListIntent.SetCategoryName(categoryName))
    }

    Scaffold {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.systemBarsPadding(),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            items(pagingData.itemCount) { index ->
                val item = pagingData[index]
                if (item != null) {
                    ProductItem(
                        product = item,
                        onItemClick = navigateToProductDetail
                    )
                }
            }

            when {
                pagingData.loadState.refresh is LoadState.Loading -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                pagingData.loadState.refresh is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) { }
                }

                pagingData.loadState.append is LoadState.Loading -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                pagingData.loadState.append is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) { }
                }
            }
        }
    }
}

@Composable
private fun ProductItem(product: Product, onItemClick: (Product) -> Unit) {
    val imagePainter = rememberImagePainter(product.image)

    Box(
        modifier = Modifier
            .bounceClickable {
                onItemClick.invoke(product)
            }
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.LightGray.copy(alpha = 0.3f)
            )
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.9f)
                    .background(color = Color.LightGray.copy(alpha = 0.3f)),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (product.price - (product.price * product.discount / 100)).toRupiah,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                )
                if (product.discount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = product.price.toRupiah,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.LightGray,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = product.discount.toString() + "%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Red
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFA500)
                    )
                    Text(
                        text = product.rating.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}