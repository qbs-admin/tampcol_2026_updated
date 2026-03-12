package com.qbrains.tampcolapp.data.network.api

import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ICheckOutApi {

    @POST(GET_DEFAULT_ADDRESS)
    fun getdefaultaddress(
        @Body getWishListRequest: GetWishListRequest
    ): Deferred<Response<GetDefaultAddressResponse>>

    @POST(APPLY_COUPON)
    fun applyCoupon(
        @Body getCouponDetailsRequest: GetCouponDetailsRequest
    ): Deferred<Response<GetCouponDetailsResponse>>


    @POST(COUPON_LIST)
    fun getCouponList(
        @Body request: CouponListRequest
    ): Deferred<Response<GetCouponListResponse>>

    @POST(SALES)
    fun placingOrders(
        @Body sendCartDataRequest: SendCartDataRequest
    ): Deferred<Response<SalesResponse>>

    @POST(CREATE_RAZORPAY_ORDER_ID)
    fun createRazorpayOrderIdAsync(
        @Body sendCartDataRequest: SendCartDataRequest
    ): Deferred<Response<SalesResponse>>

    @POST(PLACE_RAZORPAY_ORDER)
    fun placeRazorpayOrder(
        @Body placeRazorpayOrderRequest :PlaceRazorpayOrderRequest
    ): Deferred<Response<SalesResponse>>

    @POST(GET_MY_ORDER_LIST)
    fun getMyOrderList(@Body myOrderRequest: MyOrderRequest): Deferred<Response<MyOrderListResponse>>

//  @POST(GET_MY_ORDER_LIST)
//    fun checkCoupon(@Body myOrderRequest: MyOrderRequest): Deferred<Response<MyOrderListResponse>>


    companion object {
        const val GET_DEFAULT_ADDRESS: String = "default-address"

        //        const val SALES: String = "sales"
        const val SALES: String = "sale/cash-on-delivery"
        const val CREATE_RAZORPAY_ORDER_ID: String = "sale/create-online-order"
        const val PLACE_RAZORPAY_ORDER: String = "sale/finish-online-order"
        const val GET_MY_ORDER_LIST: String = "orders"
        const val APPLY_COUPON: String = "coupon-apply"
        const val COUPON_LIST: String = "coupons"
    }
}