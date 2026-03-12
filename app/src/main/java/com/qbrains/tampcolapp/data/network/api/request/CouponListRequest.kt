package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CouponListRequest {

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("total")
    @Expose
    var grandTotal: Double? = null

}