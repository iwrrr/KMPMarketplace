package com.kmp.features.product_list

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kmp.api.product.ProductPagingSources
import com.kmp.libraries.core.AppConfig
import com.kmp.libraries.core.state.Intent
import com.kmp.libraries.core.viewmodel.ViewModel
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val appConfig: AppConfig
) : ViewModel<ProductListState, ProductListIntent>(ProductListState()) {
    override fun sendIntent(intent: Intent) {
        when (intent) {
            is ProductListIntent.SetCategoryName -> {
                val categoryName = intent.categoryName
                setCategoryName(categoryName)
            }

            is ProductListIntent.GetProductList -> {
                val categoryId = intent.categoryId
                getProductList(categoryId)
            }
        }
    }

    private fun setCategoryName(categoryName: String) {
        updateUiState {
            copy(categoryName = categoryName)
        }
    }

    private fun getProductList(categoryId: Int) = viewModelScope.launch {
        val query = if (categoryId == -1) {
            ""
        } else {
            "?categoryId=$categoryId"
        }

        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ProductPagingSources(appConfig, query)
            }
        ).flow
            .cachedIn(viewModelScope)
            .also {
                updateUiState {
                    copy(pagingDataProduct = it)
                }
            }
    }
}