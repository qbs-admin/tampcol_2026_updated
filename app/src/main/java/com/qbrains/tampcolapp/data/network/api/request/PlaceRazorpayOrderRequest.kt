package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.SerializedName


data class PlaceRazorpayOrderRequest(
    @SerializedName("buyer")
    var buyer: String? = null,

    @SerializedName("coupon_amount")
    var couponAmount: Double? = null,

    @SerializedName("coupon_code")
    var couponCode: String? = null,

    @SerializedName("order_id")
    var orderId: String? = null,

    @SerializedName("payment_id")
    var paymentId: String? = null,

    @SerializedName("sale_id")
    var saleId: Int? = null,
)
