package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class SalesResponse(

    @SerializedName("couponmessage")
    val couponmessage: String? = null,

    @SerializedName("sale_id")
    val saleId: Int? = null,

    @SerializedName("order_id")
    val orderId: String? = null,

    @SerializedName("success")
    val success: String? = null,

    @SerializedName("message")
    val message: String? = null
)
