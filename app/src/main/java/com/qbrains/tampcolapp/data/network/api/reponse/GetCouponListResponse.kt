package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName


data class GetCouponListResponse(

	@SerializedName("coupons")
	val couponDetails: List<CouponItem?>? = null,

	@SerializedName("success")
	val success: Int? = null,

	@SerializedName("message")
	val message: String? = null
)

data class CouponItem(

	@SerializedName("coupon_id")
	val coupon_id: String? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("spec")
	val spec: String? = null,

//	@SerializedName("added_by")
//	val added_by: String? = null,

	@SerializedName("till")
	val till: String? = null,

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("status")
	val status: String? = null
)



