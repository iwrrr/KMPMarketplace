package com.kmp.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.api.product.ProductRepository
import com.kmp.api.product.model.category.Category
import com.kmp.api.product.model.product.Product
import com.kmp.libraries.component.utils.bounceClickable
import com.kmp.libraries.component.utils.toRupiah
import com.kmp.libraries.core.LocalAppConfig
import com.kmp.libraries.core.state.Async
import com.kmp.libraries.core.viewmodel.rememberViewModel

@Composable
fun Home() {
    val appConfig = LocalAppConfig.current
    val productRepository = remember { ProductRepository(appConfig) }
    val homeViewModel = rememberViewModel { HomeViewModel(productRepository) }

    val homeState by homeViewModel.uiState.collectAsState()

    Scaffold {
        LazyColumn(
            modifier = Modifier.systemBarsPadding()
        ) {
            item { HeaderSection() }
            item { CategorySection(homeState) }
            item { TopProductSection(homeState) }
            item { TopProductSection(homeState) }
            item { TopProductSection(homeState) }
            item { TopProductSection(homeState) }
            item { TopProductSection(homeState) }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text("Marketplace")
    }
}

@Composable
fun CategorySection(state: HomeState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 8.dp, bottom = 2.dp)
    ) {
        Text(
            text = "Kategori inspirasi belanjamu",
            fontWeight = FontWeight.Bold
        )
    }

    when (val async = state.asyncCategoryList) {
        Async.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Async.Success -> {
            LazyVerticalGrid(
                modifier = Modifier.height(150.dp),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(async.data) {
                    CategoryItem(it, onItemClick = {})
                }
            }
        }

        else -> {}
    }
}

@Composable
fun CategoryItem(category: Category, onItemClick: (Category) -> Unit) {
    Box(
        modifier = Modifier
            .bounceClickable {
                onItemClick.invoke(category)
            }
            .background(
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .height(60.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.name,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TopProductSection(state: HomeState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 8.dp, bottom = 2.dp)
    ) {
        Text(
            text = "Ini cocok buat kamu lho!",
            fontWeight = FontWeight.Bold
        )
    }

    when (val async = state.asyncProductList) {
        Async.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Async.Success -> {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(async.data) {
                    ProductItem(product = it, onItemClick = {})
                }
            }
        }

        else -> {}
    }
}

@Composable
fun ProductItem(product: Product, onItemClick: (Product) -> Unit) {
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
            .width(120.dp)
            .height(226.dp),
    ) {
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(color = Color.Black.copy(alpha = 0.3f)),
            )
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