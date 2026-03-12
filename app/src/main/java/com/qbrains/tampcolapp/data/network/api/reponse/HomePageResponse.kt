package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class HomePageResponse(

    @SerializedName("block")
    val block: List<blockItem>? = null,

    @SerializedName("featured")
    val featured: List<blockItem>? = null,

    @SerializedName("top")
    val top:List<blockItem>? = null,
)

data class blockItem(

    @field:SerializedName(value = "product_id")
    val product_id: String? = null,

    @field:SerializedName(value = "title")
    val title: String? = null,

    @field:SerializedName(value = "price_variant")
    val price_variant: ArrayList<priceVarientItem>? = null,

    @field:SerializedName(value = "wishlist")
    val wishlist: String? = null,

    @field:SerializedName(value = "category")
    val category: String? = null,

    @field:SerializedName(value = "deal")
    val deal: String? = null,

    @field:SerializedName(value = "product_deal_days")
    val productDealDays: String? = "",
)

data class priceVarientItem(

    @field:SerializedName(value = "id")
    val id: String? = null,

    @field:SerializedName(value = "product_deal_days")
    val product_deal_days: String? = null,

    @field:SerializedName(value = "product_weight")
    val product_weight: String? = null,

    @field:SerializedName(value = "discount")
    val discount: String? = null,

    @field:SerializedName(value = "price")
    val price: String? = null,

    @field:SerializedName(value = "stock")
    val stock: String? = null,

    @field:SerializedName(value = "image")
    val image: String? = null,

     @field:SerializedName(value = "discount_percent")
    val discount_percent: String? = null,
)