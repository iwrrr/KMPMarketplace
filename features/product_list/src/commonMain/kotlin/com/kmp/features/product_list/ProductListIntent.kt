package com.kmp.features.product_list

import com.kmp.libraries.core.state.Intent

sealed class ProductListIntent : Intent {
    data class GetProductList(val categoryId: Int) : ProductListIntent()
    data class SetCategoryName(val categoryName: String) : ProductListIntent()
}
