package com.qbrains.tampcolapp.data.network.api

import com.qbrains.tampcolapp.data.network.api.reponse.LoginResponse
import com.qbrains.tampcolapp.data.network.api.reponse.SignUpResponse
import com.qbrains.tampcolapp.data.network.api.request.ForgotPasswordRequest
import com.qbrains.tampcolapp.data.network.api.request.SignUpRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IRegisterApi {
    @POST(SIGN_UP)
    fun signUp(@Body signUpRequest: SignUpRequest): Deferred<Response<LoginResponse>>

    @POST(FORGET_PASSWORD)
    fun forgotPassword(@Body request: ForgotPasswordRequest): Deferred<Response<SignUpResponse>>


    companion object {

        const val SIGN_UP: String = "create-user"
        const val FORGET_PASSWORD: String = "forget-password"
    }


}