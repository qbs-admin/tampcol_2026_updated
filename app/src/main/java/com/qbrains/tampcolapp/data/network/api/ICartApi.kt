package com.qbrains.tampcolapp.data.network.api

import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ICartApi {

    @POST(GET_CART_LIST)
    fun getdefaultaddress(
        @Body getWishListRequest: GetWishListRequest
    ): Deferred<Response<GetDefaultAddressResponse>>

    @POST(ADD_CART)
    fun addCartAsync(
        @Body getCouponDetailsRequest: GetCouponDetailsRequest
    ): Deferred<Response<GetCouponDetailsResponse>>


    @POST(UPDATE_CART)
    fun updateCartAsync(
        @Body request: String
    ): Deferred<Response<GetCouponListResponse>>


    @POST(REMOVE_CART)
    fun removeCartAsync(
        @Body sendCartDataRequest: SendCartDataRequest
    ): Deferred<Response<SalesResponse>>

    @POST(CLEAR_CART)
    fun clearCartAsync(@Body myOrderRequest: MyOrderRequest): Deferred<Response<MyOrderListResponse>>

    @POST(MERGE_CART)
    fun mergeCartAsync(@Body myOrderRequest: MyOrderRequest): Deferred<Response<MyOrderListResponse>>


    companion object {
        const val GET_CART_LIST: String = "/cart/list"
        const val ADD_CART: String = "/cart/add"
        const val UPDATE_CART: String = "/cart/update"
        const val REMOVE_CART: String = "/cart/remove"
        const val CLEAR_CART: String = "/cart/clear"
        const val MERGE_CART: String = "/cart/merge"
    }
}