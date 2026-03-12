package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class CartListResponse(
    val products: List<Product>,
    val success: Int,
    val total_discount: Double,
    val shipping_cost: Double,
    val sub_total: Double,
    val total: Double
)

data class Product(
    val category: String,
    val deal: String,
    val description: String,
    @SerializedName("price_variant")
    val priceVariant: List<PriceVariant>,
    @SerializedName("product_deal_days")
    val productDealDays: String,
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("sub_category")
    val subCategory: String,
    val title: String
)

data class PriceVariant(
    val discount: String,
    @SerializedName("discount_percent")
    val discountPercent: String,
    val id: String,
    val image: String,
    val price: Double,
    @SerializedName("product_weight")
    val productWeight: String,
    val stock: String,
    @SerializedName("striked_amt")
    val strikedAmt: String ,
    @SerializedName("quantity")
    val quantity: Int
)
