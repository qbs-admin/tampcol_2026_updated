package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class GetDefaultAddressResponse(

	@SerializedName("address")
	val address: List<AddressItems>? = null,

	@SerializedName("success")
	val success: String? = null,

	@SerializedName("message")
	val message: String? = null,




)

data class AddressItems(

	@SerializedName("Pincode")
	val zip: String? = null,

	@SerializedName("country")
	val country: String? = null,

	@SerializedName("address2")
	val address2: String? = null,

	@SerializedName("city")
	val city: String? = null,

	@SerializedName("address1")
	val address1: String? = null,

	@SerializedName("created")
	val created: String? = null,

	@SerializedName("type")
	val type: String? = null,

	@SerializedName("default_address")
	val defaultAddress: String? = null,

	@SerializedName("langlat")
	val langlat: String? = null,

	@SerializedName("user_id")
	val userId: String? = null,

	@SerializedName("first_name")
	val name: String? = null,

	@SerializedName("last_name")
	val lastName: String? = null,

	@SerializedName("phone")
	val phone: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("state")
	val state: String? = null,

	@SerializedName("area")
	val area: String? = null,

	@SerializedName("shipping_cost")
    val shipping_cost: String? = null,

	@SerializedName("reward_points")
    val reward_points: String? = null,

	@SerializedName("reward_point_rate")
    val reward_point_rate: String? = null,

	@SerializedName("min_point_reach")
    val min_point_reach: String? = null,

    @SerializedName("email")
    val email: String? = null,

	@SerializedName("display")
	val display: Int? = null

)


