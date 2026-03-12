package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class MiniOrderResponse(

	@field:SerializedName("min_order_amount")
	val orderMinAmount: String? = null,

	@field:SerializedName("success")
	val success: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
