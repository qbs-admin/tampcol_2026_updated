package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OTPResponse {

    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("otp_hash")
    @Expose
    var otpHash: String? = null
}



