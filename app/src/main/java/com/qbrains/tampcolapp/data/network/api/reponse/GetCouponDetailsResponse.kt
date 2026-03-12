package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class GetCouponDetailsResponse(

    @SerializedName("deduct_amt")
    val deductAmt: Double? = null,

    @SerializedName("success")
    val success: Int? = null,

    @SerializedName("cart_count")
    val cartCount: Int? = null,

    @SerializedName("message")
    val message: String? = null


)

