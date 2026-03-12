package com.qbrains.tampcolapp.data.network.api

import retrofit2.Call
import retrofit2.http.*


interface IGoogleAPI {

    @POST("v4/token")
    fun googleLogin(
        @Body loginRequest: GoogleAuthRequest
    ): Call<GoogleAuthResponse>

}