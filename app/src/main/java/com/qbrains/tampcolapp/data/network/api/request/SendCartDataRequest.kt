package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.SerializedName


data class SendCartDataRequest(

    @SerializedName("buyer")
    var buyer: String? = null,

    @SerializedName("payment_type")
    var paymentType: String? = null,

    @SerializedName("grand_total")
    var grandTotal: String? = null,

    @SerializedName("shipping_address")
    var shippingAddress: ShippingAddress? = null,

/*	@SerializedName("productdetails")
	var productDetails: List<ProductDetailsItem?>? = null,*/

    @SerializedName("coupon_amount")
    var couponAmount: Double? = null,

    @SerializedName("coupon_code")
    var couponCode: String? = null,

    @SerializedName("shipping_cost")
    var shipping_cost: Double? = null,

    /*@SerializedName("gateway_txnid")
    var gatewayTxnid: String? = null,

    @SerializedName("gateway_payment")
    var gatewayPayment: String? = null,*/

    @SerializedName("subTotal")
    var subTotal: String? = null,


//"gateway_payment":{}

)


data class ProductDetailsItem(
//	"product_id":1933,
//                "product_weight":"100g",
//                "product_price_id":88,
//                "qty":5


    @SerializedName("product_id")
    val product_id: String? = null,

    @SerializedName("product_weight")
    val product_weight: String? = null,

    @SerializedName("product_price_id")
    val product_price_id: String? = null,

    @SerializedName("qty")
    val qty: Int? = null,

    )

data class ShippingAddress(

    @SerializedName("Pincode")
    val zip: String? = null,

    @SerializedName("firstname")
    val firstname: String? = null,

    @SerializedName("delivery_date")
    val deliveryDate: String? = null,

    @SerializedName("payment_type")
    val paymentType: String? = null,

//	@SerializedName("langlat")
//	val langlat: String? = null,

    @SerializedName("address2")
    val address2: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("address1")
    val address1: String? = null,

//	@SerializedName("delivery_timeslot")
//	val deliveryTimeslot: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("lastname")
    val lastname: String? = null,

    @SerializedName("country")
    val country: String? = "India",

    @SerializedName("state")
    val state: String? = "",

    @SerializedName("area")
    val area: String? = ""
)
