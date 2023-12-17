package com.kmp.features.home

import com.kmp.libraries.core.state.Intent

sealed class HomeIntent : Intent {
    data object GetCategoryList : HomeIntent()
    data object GetProductList : HomeIntent()
}