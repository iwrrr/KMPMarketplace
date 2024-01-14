package com.kmp.api.product.model

import com.kmp.api.product.model.category.Category
import com.kmp.api.product.model.category.CategoryResponse
import com.kmp.api.product.model.product.product.Product
import com.kmp.api.product.model.product.product.ProductRealm
import com.kmp.api.product.model.product.product.ProductResponse
import com.kmp.api.product.model.product.product_detail.ProductDetail
import com.kmp.api.product.model.product.product_detail.ProductDetailResponse
import com.kmp.api.product.model.product.product_detail.UserReview

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

    fun mapItemResponseToProduct(itemResponse: ProductResponse.DataResponse?): Product {
        return Product(
            id = itemResponse?.id ?: 0,
            name = itemResponse?.name.orEmpty(),
            price = itemResponse?.price ?: 0.0,
            image = itemResponse?.images.orEmpty(),
            discount = itemResponse?.discount ?: 0,
            rating = itemResponse?.rating ?: 0.0,
        )
    }

    fun mapResponseToProductDetail(itemResponse: ProductDetailResponse): ProductDetail {
        return ProductDetail(
            id = itemResponse.data?.id ?: 0,
            category = mapItemResponseToCategory(itemResponse.data?.category),
            description = itemResponse.data?.description.orEmpty(),
            discount = itemResponse.data?.discount ?: 0,
            images = itemResponse.data?.images.orEmpty(),
            name = itemResponse.data?.name.orEmpty(),
            price = itemResponse.data?.price ?: 0.0,
            rating = itemResponse.data?.rating ?: 0.0,
            userReview = itemResponse.data?.userReview?.map(::mapResponseToUserReviewList)
                .orEmpty(),
        )
    }

    private fun mapItemResponseToCategory(itemResponse: ProductDetailResponse.DataResponse.CategoryResponse?): Category {
        return Category(
            id = itemResponse?.id ?: 0,
            name = itemResponse?.name.orEmpty(),
            description = itemResponse?.description.orEmpty(),
        )
    }

    private fun mapResponseToUserReviewList(itemResponse: ProductDetailResponse.DataResponse.UserReviewResponse?): UserReview {
        return UserReview(
            review = itemResponse?.review.orEmpty(),
            user = itemResponse?.user.orEmpty(),
        )
    }

    fun mapProductDetailToRealm(productDetail: ProductDetail): ProductRealm {
        return ProductRealm()
            .apply {
                id = productDetail.id
                description = productDetail.description
                discount = productDetail.discount
                name = productDetail.name
                price = productDetail.price
                rating = productDetail.rating
                image = productDetail.images.first()
            }
    }

    fun mapRealmToProduct(productRealm: ProductRealm): Product {
        return Product(
            id = productRealm.id,
            name = productRealm.name,
            price = productRealm.price,
            image = productRealm.image,
            discount = productRealm.discount,
            rating = productRealm.rating,
        )
    }
}