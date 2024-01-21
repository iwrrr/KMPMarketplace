package com.kmp.features.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.api.product.LocalProductRepository
import com.kmp.api.product.model.cart.CartProduct
import com.kmp.libraries.component.ui.CustomAppBar
import com.kmp.libraries.component.utils.toRupiah
import com.kmp.libraries.core.state.Async
import com.kmp.libraries.core.viewmodel.rememberViewModel
import com.seiko.imageloader.rememberImagePainter

@Composable
fun Cart(
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val repository = LocalProductRepository.current
    val viewModel = rememberViewModel { CartViewModel(repository) }

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(CartIntent.GetCartList)
    }

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Keranjang",
                backgroundColor = Color.White,
                contentPadding = WindowInsets
                    .systemBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                    .asPaddingValues(),
                elevation = 0.dp,
                onNavigationClick = navigateBack
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val async = state.asyncCartProductList) {
                Async.Loading -> {
                    CircularProgressIndicator()
                }

                is Async.Success -> {
                    LazyColumn(
                        modifier = Modifier.navigationBarsPadding(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(async.data) { cart ->
                            if (cart != null) {
                                CartItem(cart)
                            }
                        }
                    }
                }

                is Async.Failure -> {
                    when (val message = async.throwable.message.toString()) {
                        "Unauthorized" -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Ups, kamu belum login!")
                                Button(
                                    modifier = Modifier.height(40.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        disabledElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp
                                    ),
                                    onClick = navigateToLogin
                                ) {
                                    Text(text = "Login")
                                }
                            }
                        }

                        else -> {
                            Text(text = message)
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun CartItem(cart: CartProduct) {
    val imagePainter = rememberImagePainter(cart.productDetail.images.first())

    Card(
        modifier = Modifier.shadow(
            elevation = 12.dp,
            shape = RoundedCornerShape(16.dp),
            spotColor = Color.LightGray.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 4.dp)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = cart.productDetail.name,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Qty: ${cart.quantity}",
                    fontSize = 14.sp
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = cart.amount.toRupiah,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}