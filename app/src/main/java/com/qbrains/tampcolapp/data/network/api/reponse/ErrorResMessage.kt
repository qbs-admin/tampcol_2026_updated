package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class ErrorResMessage(

    @field:SerializedName(value = "message"/*,alternate = ["error_description"]*/)
    val message: String? = null,

    @field:SerializedName(value = "success"/*,alternate = ["error_description"]*/)
    val success: String? = null,

    @SerializedName("cart_count")
    val cartCount: Int? = 0

)