package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.qbrains.tampcolapp.data.network.api.IRegisterApi
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.LoginResponse
import com.qbrains.tampcolapp.data.network.api.reponse.SignUpResponse
import com.qbrains.tampcolapp.data.network.api.request.ForgotPasswordRequest
import com.qbrains.tampcolapp.data.network.api.request.SignUpRequest
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.utility.Provider
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess

interface IRegisterRepository {
    fun signUp(
        signUpRequest: SignUpRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<ErrorResMessage>
    )

    fun forgotPassword(
        forgotPassword: ForgotPasswordRequest,
        onSuccess: OnSuccess<SignUpResponse>,
        onError: OnError<Any>
    )
    companion object : Provider<RegisterRepository>() {
        override fun create(args: Array<out Any>): RegisterRepository {
            if (args.size != 3) throw IllegalArgumentException("args size must be 4")

            val context = if (args[0] !is Context)
                throw IllegalArgumentException("args[0] is not Context")
            else
                args[0] as Context

            val api = if (args[1] !is IRegisterApi)
                throw IllegalArgumentException("args[1] is not ArticleDao")
            else
                args[1] as IRegisterApi

            val iPref = if (args[2] !is IPreferenceManager)
                throw IllegalArgumentException("args[2] is not IAPIWelcome")
            else
                args[2] as IPreferenceManager

            return RegisterRepository(context, api,  iPref)
        }
    }
}