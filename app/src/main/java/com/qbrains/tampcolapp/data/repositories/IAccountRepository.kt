package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.qbrains.tampcolapp.data.network.api.IAccountApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.utility.Provider
import com.qbrains.tampcolapp.data.viewmodel.ChangePasswordResponse
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import com.qbrains.tampcolapp.ui.fragments.ChangePasswordRequest

interface IAccountRepository {

    fun sendToken(
        onRequest: SendTokenRequest,
        onResponse: OnSuccess<SendTokenResponse>,
        onError: OnError<Any>
    )

    fun getContactDetails(
        onResponse: OnSuccess<ContactusResponse>,
        onError: OnError<Any>
    )

    fun addAddress(
        addAddressRequest: AddAddressRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    )

    fun editProfile(
        editProfileRequest: EditProfileRequest,
        onSuccess: OnSuccess<EditProfileResponse>,
        onError: OnError<Any>
    )

    fun getProfile(
        onSuccess: OnSuccess<GetProfileResponse>,
        onError: OnError<Any>
    )

    fun getAddress(
        onSuccess: OnSuccess<GetAddressResponse>,
        onError: OnError<Any>
    )

    fun pinCode(
        pincodeRequest: PincodeRequest,
        onSuccess: OnSuccess<PincodeResponse>,
        onError: OnError<Any>
    )

    fun getAddressDetails(
        getAddressDetailsRequest: GetAddressDetailsRequest,
        onSuccess: OnSuccess<GetDefaultAddressResponse>,
        onError: OnError<Any>
    )

    fun updateAddress(
        updateAddressRequest: UpdateAddressRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    )

    fun deleteAddress(
        getAddressDetailsRequest: GetAddressDetailsRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    )

    fun changePasswordAsync(
        changePasswordRequest: ChangePasswordRequest,
        onSuccess: OnSuccess<ChangePasswordResponse>,
        onError: OnError<Any>
    )

    companion object : Provider<IAccountRepository>() {
        override fun create(args: Array<out Any>): AccountRepository {
            if (args.size != 3) throw IllegalArgumentException("args size must be 4")

            val context = if (args[0] !is Context)
                throw IllegalArgumentException("args[0] is not Context")
            else
                args[0] as Context

            val api = if (args[1] !is IAccountApi)
                throw IllegalArgumentException("args[1] is not ArticleDao")
            else
                args[1] as IAccountApi

            val iPref = if (args[2] !is IPreferenceManager)
                throw IllegalArgumentException("args[2] is not IAPIWelcome")
            else
                args[2] as IPreferenceManager

            return AccountRepository(context, api, iPref)
        }
    }
}