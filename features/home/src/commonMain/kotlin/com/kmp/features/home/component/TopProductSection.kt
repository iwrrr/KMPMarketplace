package com.kmp.features.home.component

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
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
import com.kmp.api.product.model.product.product.Product
import com.kmp.features.home.viewmodel.HomeState
import com.kmp.libraries.component.utils.bounceClickable
import com.kmp.libraries.component.utils.fullOffset
import com.kmp.libraries.component.utils.toRupiah
import com.kmp.libraries.core.state.Async
import com.seiko.imageloader.rememberImagePainter

@Composable
fun TopProductSection(
    state: HomeState,
    onItemClick: (Product) -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Ini cocok buat kamu lho!",
            fontWeight = FontWeight.Bold
        )

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
                    modifier = Modifier
                        .fullOffset(16)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    items(async.data) {
                        ProductItem(
                            product = it,
                            onItemClick = onItemClick
                        )
                    }
                }
            }

            else -> {}
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
                            color = Color.Gray,
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
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFBF00)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
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