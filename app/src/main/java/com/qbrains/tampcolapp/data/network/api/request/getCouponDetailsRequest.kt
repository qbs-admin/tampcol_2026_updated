package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetCouponDetailsRequest {

    @SerializedName("couponcode")
    @Expose
    var couponcode: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("total")
    @Expose
    var grandTotal: Int? = null

    @SerializedName("shipping_cost")
    @Expose
    var shippingCost: Int? = null

    @SerializedName("products")
    @Expose
    var products: List<CouponProductItem>? = null

}

data class CouponProductItem(

    @SerializedName("product_price_id")
    val productPriceId: Int? = null,

    @SerializedName("qty")
    val qty: Int? = null,

    )
