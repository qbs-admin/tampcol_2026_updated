package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartListRequest {

    @SerializedName("device_token")
    @Expose
    var deviceToken: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

}
