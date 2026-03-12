package com.qbrains.tampcolapp.data.network.api

import com.google.gson.annotations.SerializedName

data class GoogleAuthResponse(
    @SerializedName("access_token")
    var accessToken: String,
    @SerializedName("expires_in")
    var expiresIn: Int,
    @SerializedName("refresh_token")
    var refreshToken: String,
    var scope: String,
    @SerializedName("token_type")
    var tokenType: String,
    @SerializedName("id_token")
    var idToken: String,
)