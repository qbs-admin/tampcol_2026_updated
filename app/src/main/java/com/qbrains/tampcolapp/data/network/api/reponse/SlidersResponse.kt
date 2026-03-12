package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName


data class SlidersResponse(

	@SerializedName("slides")
	val slides: List<SlidesItem?>? = null,

	@SerializedName("success")
	val success: Int? = null,

	@SerializedName("message")
	val message: String? = null
)

data class SlidesItem(
	@SerializedName("images")
	val images: String? = null
)
