//package com.qbrains.harina.data.network.api
//
//import com.qbrains.harina.data.network.api.reponse.*
//import com.qbrains.harina.data.network.api.request.*
//import kotlinx.coroutines.Deferred
//import retrofit2.Response
//import retrofit2.http.*
//
//interface INotifiApi {
//
//    @POST(NOTIFI)
//    fun sendToken(
//        @Body editProfileRequest: EditProfileRequest
//    ): Deferred<Response<EditProfileResponse>>
//
//
//
//    companion object {
//        const val NOTIFI: String = "editprofile.php"
//    }
//}
//abc