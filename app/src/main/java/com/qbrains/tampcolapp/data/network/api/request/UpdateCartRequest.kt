package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateCartRequest {

    @SerializedName("device_token")
    @Expose
    var deviceToken: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("product_price_id")
    @Expose
    var productPriceId: String? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

}
