package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyOrderRequest {

    @SerializedName("buyer")
    @Expose
    var id: String? = null
}