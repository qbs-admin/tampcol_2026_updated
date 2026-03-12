package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class WishListProductItem {
     @SerializedName("product_id")
    @Expose
    val productId: String = ""
    @SerializedName("stock")
    @Expose
    val stock: String = ""
    @SerializedName("image")
    @Expose
    val image: String = ""
     @SerializedName("product_name")
    @Expose
    val productName: String = ""


}