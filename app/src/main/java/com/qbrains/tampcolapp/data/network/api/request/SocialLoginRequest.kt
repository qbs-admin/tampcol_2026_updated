package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SocialLoginRequest {
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null
}
