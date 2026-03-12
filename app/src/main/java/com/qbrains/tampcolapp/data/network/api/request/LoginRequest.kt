package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class  LoginRequest {

    @SerializedName("email")
    @Expose
    var email : String?=null

    @SerializedName("password")
    @Expose
    var password : String?=null
}

class loginwithOtpRequest {

    @SerializedName("phone")
    @Expose
    var phone : String?=null

}
class verifyOtpRequest {

    @SerializedName("otp")
    @Expose
    var otp : String?=null

    @SerializedName("phone")
    @Expose
    var otp_hash : String?=null


}