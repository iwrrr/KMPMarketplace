package com.kmp.features.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.api.product.model.category.Category
import com.kmp.features.home.viewmodel.HomeState
import com.kmp.libraries.component.utils.bounceClickable
import com.kmp.libraries.core.state.Async

@Composable
fun CategorySection(state: HomeState, onCategoryClick: (Category) -> Unit) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Kategori inspirasi belanjamu",
            fontWeight = FontWeight.Bold
        )

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
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    userScrollEnabled = false
                ) {
                    items(async.data) {
                        CategoryItem(it, onItemClick = onCategoryClick)
                    }
                }
            }

            else -> {}
        }
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
                fontSize = 10.sp
            )
        }
    }
}
