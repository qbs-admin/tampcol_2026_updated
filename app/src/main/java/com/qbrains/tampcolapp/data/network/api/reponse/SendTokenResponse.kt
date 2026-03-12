package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SendTokenResponse {

    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("result")
    @Expose
    var result: String? = null
}


