package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName


data class ProductListResponse(


@SerializedName("success")
val success: Int? = null,


@SerializedName("message")
val message: String? = null,

@SerializedName("products")
val products: List<ProductItem>? = null
)



data class ProductItem(

    @SerializedName("product_id")
    val product_id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("sub_category")
    val sub_category: String? = null,

    @SerializedName("deal")
    val deal: String? = null,

    @SerializedName("price_variant")
    val price_variant: List<varient>? = null,


    )

data class varient(


    @SerializedName("product_weight")
    val product_weight: String? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("price")
    val price: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("stock")
    val stock: String? = null,


    @SerializedName("discount")
    val discount: String? = null,

 @SerializedName("discount_percent")
    val discount_percent: String? = null


)
