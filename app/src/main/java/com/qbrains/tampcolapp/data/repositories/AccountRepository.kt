package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.google.gson.Gson
import com.qbrains.tampcolapp.data.network.api.IAccountApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.ChangePasswordResponse
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import com.qbrains.tampcolapp.ui.fragments.ChangePasswordRequest
import kotlinx.coroutines.*

class AccountRepository(
    private val context: Context,
    private val iAccountApi: IAccountApi,
    private val iPref: IPreferenceManager
) : IAccountRepository {


    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.IO + mJob)

    override fun sendToken(onRequest:SendTokenRequest,onResponse: OnSuccess<SendTokenResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val response=iAccountApi.sendToken(onRequest).await()
                if (response.isSuccessful){
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onResponse(it) }
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun getContactDetails(
        onResponse: OnSuccess<ContactusResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response=iAccountApi.getContactusDetails().await()
                if (response.isSuccessful){
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onResponse(it) }
                    }
                }
            }catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun addAddress(
        addAddressRequest: AddAddressRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iAccountApi.addAddress(addAddressRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }
            }
        }
    }

    override fun editProfile(
        editProfileRequest: EditProfileRequest,
        onSuccess: OnSuccess<EditProfileResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iAccountApi.editProfile(editProfileRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun getProfile(onSuccess: OnSuccess<GetProfileResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val getWishListRequest = GetWishListRequest()
                getWishListRequest.userId = iPref.getCustomerId()
                val response = iAccountApi.getProfile(getWishListRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }

    }

    override fun getAddress(onSuccess: OnSuccess<GetAddressResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val getWishListRequest = GetWishListRequest()
                getWishListRequest.userId = iPref.getCustomerId()
                val response = iAccountApi.getAddress(getWishListRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }

    }

    override fun pinCode(
        pincodeRequest: PincodeRequest,
        onSuccess: OnSuccess<PincodeResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iAccountApi.pinCode(pincodeRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun getAddressDetails(
        getAddressDetailsRequest: GetAddressDetailsRequest,
        onSuccess: OnSuccess<GetDefaultAddressResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iAccountApi.getAddressDetails(getAddressDetailsRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun updateAddress(
        updateAddressRequest: UpdateAddressRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iAccountApi.updateAddress(updateAddressRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun deleteAddress(
        getAddressDetailsRequest: GetAddressDetailsRequest,
        onSuccess: OnSuccess<AddAddressResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iAccountApi.deleteAddress(getAddressDetailsRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun changePasswordAsync(
        changePasswordRequest: ChangePasswordRequest,
        onSuccess: OnSuccess<ChangePasswordResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iAccountApi.changePasswordAsync(changePasswordRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }


}