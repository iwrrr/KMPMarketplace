package com.kmp.api.product.model

import com.kmp.api.product.model.category.Category
import com.kmp.api.product.model.category.CategoryResponse
import com.kmp.api.product.model.product.Product
import com.kmp.api.product.model.product.ProductResponse

object Mapper {

    fun mapResponseToCategoryList(productListResponse: CategoryResponse?): List<Category> {
        return productListResponse?.data?.map(::mapItemResponseToCategory).orEmpty()
    }

    private fun mapItemResponseToCategory(itemResponse: CategoryResponse.DataResponse?): Category {
        return Category(
            id = itemResponse?.id ?: 0,
            name = itemResponse?.name.orEmpty(),
            description = itemResponse?.description.orEmpty(),
        )
    }

    fun mapResponseToProductList(productResponse: ProductResponse?): List<Product> {
        return productResponse?.data?.map(::mapItemResponseToProduct).orEmpty()
    }

    private fun mapItemResponseToProduct(itemResponse: ProductResponse.DataResponse?): Product {
        return Product(
            id = itemResponse?.id ?: 0,
            name = itemResponse?.name.orEmpty(),
            price = itemResponse?.price ?: 0.0,
            image = itemResponse?.images.orEmpty(),
            discount = itemResponse?.discount ?: 0,
            rating = itemResponse?.rating ?: 0.0,
        )
    }
}