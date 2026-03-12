package com.qbrains.tampcolapp.data.network.api

import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.viewmodel.ChangePasswordResponse
import com.qbrains.tampcolapp.ui.fragments.ChangePasswordRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface IAccountApi {
    @POST(NOTIFI)
    fun sendToken(
        @Body request: SendTokenRequest
    ): Deferred<Response<SendTokenResponse>>


    @POST(EDIT_PROFILE)
    fun editProfile(
        @Body editProfileRequest: EditProfileRequest
    ): Deferred<Response<EditProfileResponse>>


    @POST(GET_PROFILE)
    fun getProfile(
        @Body getWishListRequest: GetWishListRequest
    ): Deferred<Response<GetProfileResponse>>

    @POST(GET_ADDRESS)
    fun getAddress(
        @Body getWishListRequest: GetWishListRequest
    ): Deferred<Response<GetAddressResponse>>

    @POST(ADD_ADDRESS)
    fun addAddress(
        @Body addAddressRequest: AddAddressRequest
    ): Deferred<Response<AddAddressResponse>>

    @POST(UPDATE_ADDRESS)
    fun updateAddress(
        @Body addAddressRequest: UpdateAddressRequest
    ): Deferred<Response<AddAddressResponse>>

    @POST(DELETE_ADDRESS)
    fun deleteAddress(
        @Body getAddressDetailsRequest: GetAddressDetailsRequest
    ): Deferred<Response<AddAddressResponse>>

    @POST(PIN_CODE)
    fun pinCode(
        @Body pincodeRequest: PincodeRequest
    ): Deferred<Response<PincodeResponse>>

    @POST(GET_ADDRESS_DETAILS)
    fun getAddressDetails(
        @Body getAddressDetailsRequest: GetAddressDetailsRequest
    ): Deferred<Response<GetDefaultAddressResponse>>

    @GET(CONTACT_US)
    fun getContactusDetails(): Deferred<Response<ContactusResponse>>

    @POST(CHANGE_PASSWORD)
    fun changePasswordAsync(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Deferred<Response<ChangePasswordResponse>>

    companion object {
        const val EDIT_PROFILE: String = "editprofile"
        const val GET_PROFILE: String = "user-profile"
        const val GET_ADDRESS: String = "address"
        const val ADD_ADDRESS: String = "add-address"
        const val PIN_CODE: String = "valid-area"
        const val GET_ADDRESS_DETAILS: String = "address-detail"
        const val UPDATE_ADDRESS: String = "update-address"
        const val DELETE_ADDRESS: String = "delete-address"
        const val CONTACT_US: String = "contact-address"
        const val NOTIFI: String = "notifications"
        const val CHANGE_PASSWORD: String = "change-password"
    }
}