package com.kmp.features.product_detail.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kmp.api.product.LocalProductRepository
import com.kmp.api.product.model.product.product_detail.ProductDetail
import com.kmp.features.product_detail.viewmodel.ProductDetailIntent
import com.kmp.features.product_detail.viewmodel.ProductDetailViewModel
import com.kmp.libraries.component.utils.BackHandler
import com.kmp.libraries.component.utils.bounceClickable
import com.kmp.libraries.component.utils.toRupiah
import com.kmp.libraries.core.state.Async
import com.kmp.libraries.core.viewmodel.rememberViewModel
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.launch

@Composable
fun ProductDetail(
    productId: Int,
    navigateBack: () -> Unit
) {
    val productRepository = LocalProductRepository.current
    val viewModel = rememberViewModel(isRetain = false) {
        ProductDetailViewModel(productRepository)
    }

    val state by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()
    val snackBarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val isScrolled = listState.firstVisibleItemScrollOffset > 10

    val appBarColor by animateColorAsState(
        targetValue = if (isScrolled) Color.White else Color.Transparent,
        animationSpec = tween(200, easing = LinearEasing)
    )

    val contentColor by animateColorAsState(
        targetValue = if (isScrolled) Color.Black else Color.White,
        animationSpec = tween(200, easing = LinearEasing)
    )

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ProductDetailIntent.GetProductDetail(productId))
    }

    LaunchedEffect(state.asyncAddToCart) {
        when (val async = state.asyncAddToCart) {
            is Async.Success -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "Berhasil menambahkan produk ke keranjang",
                        duration = SnackbarDuration.Short,
                    )
                }
            }

            is Async.Failure -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = async.throwable.message.toString(),
                        duration = SnackbarDuration.Short,
                    )
                }
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            when (val async = state.asyncProductDetail) {
                Async.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is Async.Success -> {
                    ProductDetailContent(
                        listState = listState,
                        productDetail = async.data,
                        isFavorite = state.isFavorite,
                        onToggleFavoriteClick = {
                            viewModel.sendIntent(
                                ProductDetailIntent.ToggleFavorite(it)
                            )
                        },
                        onAddToCartClick = {
                            viewModel.sendIntent(
                                ProductDetailIntent.AddToCart(it.id, 1)
                            )
                        }
                    )
                }

                is Async.Failure -> {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = async.throwable.message.orEmpty()
                    )
                }

                else -> {}
            }

            TopAppBar(
                backgroundColor = appBarColor,
                contentColor = contentColor,
                contentPadding = WindowInsets
                    .systemBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                    .asPaddingValues(),
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = null
                            )
                        }
                    }
                },
                elevation = 0.dp
            )

            when (state.asyncAddToCart) {
                Async.Loading -> {
                    Dialog(
                        onDismissRequest = { },
                        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(100.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProductDetailContent(
    listState: LazyListState,
    productDetail: ProductDetail,
    isFavorite: Boolean,
    onToggleFavoriteClick: (productDetail: ProductDetail) -> Unit,
    onAddToCartClick: (productDetail: ProductDetail) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BackHandler(scaffoldState.bottomSheetState.isExpanded) {
        scope.launch {
            scaffoldState.bottomSheetState.collapse()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Swipe up to expand sheet")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.collapse() }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    ProductImageSection(images = productDetail.images)
                }
                item {
                    ProductInfoSection(
                        productDetail = productDetail,
                        isFavorite = isFavorite,
                        onToggleFavoriteClick = onToggleFavoriteClick
                    )
                }
                item {
                    Divider(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        thickness = 8.dp
                    )
                }
                item {
                    ProductDetailSection(
                        productDetail = productDetail,
                        onReadMoreClick = {
                            scope.launch { scaffoldState.bottomSheetState.expand() }
                        }
                    )
                }
                item {
                    Divider(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        thickness = 8.dp
                    )
                }
                item {
                    ProductReviewSection(
                        productDetail = productDetail,
                        onSeeAllClick = {}
                    )
                }
            }

            BottomAppBar(
                backgroundColor = Color.White,
                contentPadding = WindowInsets.navigationBars.asPaddingValues()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
                        onClick = {}
                    ) {
                        Text(text = "Beli Langsung", letterSpacing = 0.sp)
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp,
                            hoveredElevation = 0.dp,
                            focusedElevation = 0.dp
                        ),
                        onClick = {
                            onAddToCartClick.invoke(productDetail)
                        }
                    ) {
                        Text(text = "+ Keranjang", letterSpacing = 0.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductImageSection(
    images: List<String>
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { images.size }
    )

    HorizontalPager(
        state = pagerState,
        pageContent = {
            val imagePainter = rememberImagePainter(images[it])

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent),
                            endY = size.height / 3
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    },
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProductInfoSection(
    productDetail: ProductDetail,
    isFavorite: Boolean,
    onToggleFavoriteClick: (productDetail: ProductDetail) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = productDetail.name,
            fontWeight = FontWeight.Normal,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = productDetail.price.toRupiah,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                modifier = Modifier.bounceClickable { onToggleFavoriteClick(productDetail) },
                imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null
            )
        }
        if (productDetail.discount > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Red.copy(alpha = 0.1f))
                        .clip(RoundedCornerShape(8.dp))
                        .padding(2.dp),
                ) {
                    Text(
                        text = productDetail.discount.toString() + "%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = productDetail.price.toRupiah,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Terjual ${productDetail.userReview.size}",
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Chip(
                modifier = Modifier.wrapContentWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.LightGray
                ),
                colors = ChipDefaults.outlinedChipColors(backgroundColor = Color.White),
                onClick = {},
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFBF00)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = productDetail.rating.toString()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${productDetail.userReview.size})"
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailSection(
    productDetail: ProductDetail,
    onReadMoreClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Deskripsi produk",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onReadMoreClick
            )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = productDetail.description,
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Baca selengkapnya",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF10B722)
            )
        }
    }
}

@Composable
private fun ProductReviewSection(
    productDetail: ProductDetail,
    onSeeAllClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ulasan pembeli",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onSeeAllClick
                ),
                text = "Lihat Semua",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF10B722)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            Text(
                text = productDetail.userReview.first().user,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = productDetail.userReview.first().review,
                fontSize = 13.sp
            )
        }
    }
}