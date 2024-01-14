package com.kmp.features.favorite.viewmodel

import com.kmp.libraries.core.state.Intent

sealed class FavoriteIntent : Intent {
    data object GetFavoriteProducts : Intent
}