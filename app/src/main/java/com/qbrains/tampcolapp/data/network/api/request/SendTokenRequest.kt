package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.SerializedName

class SendTokenRequest  {

    @SerializedName("user_id")
    var userId : String?=null

    @SerializedName("token_id")
    var tokenId : String?=null


}


// {"success":"1","message":"success","result":"success"}