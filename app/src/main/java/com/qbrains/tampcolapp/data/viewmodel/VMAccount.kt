package com.qbrains.tampcolapp.data.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.network.api.IAccountApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.network.manager.NetworkingManager
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.repositories.IAccountRepository
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import com.qbrains.tampcolapp.ui.fragments.ChangePasswordRequest
import org.jetbrains.annotations.NotNull

class VMAccount(
    private val context: Context,
    @NotNull private val repository: IAccountRepository
) :
    ViewModel() {

    val isNewUpdateAvailable = ObservableField(true)

    fun sendToken(
        onRequest:SendTokenRequest,
        onSuccess: OnSuccess<SendTokenResponse>,
        onError: OnError<Any>
    ) {
        repository.sendToken( onRequest,onSuccess, onError)
    }

    fun getProfile(
        onSuccess: OnSuccess<GetProfileResponse>,
        onError: OnError<Any>
    ) {
        repository.getProfile( onSuccess, onError)
    }
    fun getAddress(
        onSuccess: OnSuccess<GetAddressResponse>,
        onError: OnError<Any>
    ) {
        repository.getAddress( onSuccess, onError)
    }
    fun pinCode(
        pincodeRequest: PincodeRequest,
        onSuccess: OnSuccess<PincodeResponse>,
        onError: OnError<Any>
    ){
        repository.pinCode(pincodeRequest, onSuccess, onError)
    }

    fun addAddress(
        addAddressRequest: AddAddressRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    ){
        repository.addAddress(addAddressRequest, onSuccess, onError)
    }

    fun getAddressDetails(
        getAddressDetailsRequest: GetAddressDetailsRequest,
        onSuccess: OnSuccess<GetDefaultAddressResponse>,
        onError: OnError<Any>
    ) {
        repository.getAddressDetails(getAddressDetailsRequest,onSuccess,onError)
    }

    fun updateAddress(
        updateAddressRequest: UpdateAddressRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    ){
        repository.updateAddress(updateAddressRequest,onSuccess,onError)
    }
    fun deleteAddress(
        getAddressDetailsRequest: GetAddressDetailsRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    ){
        repository.deleteAddress(getAddressDetailsRequest,onSuccess,onError)
    }

    fun changePassword(
        changePasswordRequest: ChangePasswordRequest,
        onSuccess: OnSuccess<ChangePasswordResponse>,
        onError: OnError<Any>
    ) = repository.changePasswordAsync(changePasswordRequest, onSuccess, onError)


    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {

        private val repository = IAccountRepository.get(
            context,
//            database,
            NetworkingManager.createApi<IAccountApi>(context),
            PreferenceManager(context)
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return VMAccount(context, repository) as T
        }
    }
}