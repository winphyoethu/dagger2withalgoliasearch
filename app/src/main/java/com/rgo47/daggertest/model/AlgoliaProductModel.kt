package com.rgo47.daggertest.model

import com.squareup.moshi.Json

data class AlgoliaProductModel(
    @Json(name = "id") val id: Int?,
    @Json(name = "name_en") val name_en: String?,
    @Json(name = "name_mm") val name_mm: String?,
    @Json(name = "sku") val sku: String?,
    @Json(name = "description_mm") val description_mm: String?,
    @Json(name = "description_en") val description_en: String?,
    @Json(name = "regular_price") val regular_price: Int?,
    @Json(name = "discount_price") val discount_price: String?,
    @Json(name = "image_origin_url") val image_origin_url: String?,
    @Json(name = "image_medium_url") val image_medium_url: String?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "brand") val brand: String,
    @Json(name = "discount_from") val discount_from: String?,
    @Json(name = "discount_to") val discount_to: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "deleted_at") val deleted_at: String?,
    @Json(name = "promotion_image") val promotion_image: String?
)