package com.kmp.api.product

import com.kmp.api.product.model.Mapper
import com.kmp.api.product.model.product.product.Product
import com.kmp.api.product.model.product.product.ProductRealm
import com.kmp.api.product.model.product.product_detail.ProductDetail
import com.kmp.libraries.core.local.LocalDataSources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductFavoriteDataSources : LocalDataSources(ProductRealm::class) {

    suspend fun insertProduct(productDetail: ProductDetail) {
        val realm = Mapper.mapProductDetailToRealm(productDetail)
        return insertObject(realm)
    }

    suspend fun removeProduct(productId: Int) {
        removeObject(ProductRealm::class, productId)
    }

    suspend fun checkIsFavorite(productId: Int): Flow<Boolean> {
        return getObjectExistById(ProductRealm::class, productId)
    }

    suspend fun getFavoriteProducts(): Flow<List<Product>> {
        return getObjects(ProductRealm::class)
            .map {
                it.map(Mapper::mapRealmToProduct)
            }
    }
}