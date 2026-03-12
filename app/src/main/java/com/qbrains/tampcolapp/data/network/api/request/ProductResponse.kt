package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.qbrains.tampcolapp.data.network.api.reponse.ProductItems

class ProductResponse {

    @SerializedName("categories")
    @Expose
    val categories: List<ProductCategory>? = null

    @SerializedName("products")
    @Expose
    val products: List<ProductItems>? = null


}

class ProductCategory {

    @SerializedName("category_id")
    @Expose
    val categoryId: String? = null

    @SerializedName("category_name")
    val categoryName: String? = null
}