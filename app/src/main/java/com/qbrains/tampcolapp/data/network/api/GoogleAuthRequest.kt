package com.qbrains.tampcolapp.data.network.api

data class GoogleAuthRequest(
    var grant_type: String,
    var client_id: String,
    var client_secret: String,
    var redirect_uri: String,
    var code: String,
)