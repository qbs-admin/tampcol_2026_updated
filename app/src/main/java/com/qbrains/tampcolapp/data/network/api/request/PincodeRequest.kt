package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PincodeRequest  {

    @SerializedName("area")
    @Expose
    var zipcode : String?=null

}