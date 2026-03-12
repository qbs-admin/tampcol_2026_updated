package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.qbrains.tampcolapp.data.network.api.ICheckOutApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.CouponListRequest
import com.qbrains.tampcolapp.data.network.api.request.GetCouponDetailsRequest
import com.qbrains.tampcolapp.data.network.api.request.PlaceRazorpayOrderRequest
import com.qbrains.tampcolapp.data.network.api.request.SendCartDataRequest
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.utility.Provider
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess

interface ICheckOutRepository {
    fun getDefaultAddress(
        onSuccess: OnSuccess<GetDefaultAddressResponse>,
        onError: OnError<Any>
    )

    fun placingOrders(
        sendCartDataRequest: SendCartDataRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    )

    fun createRazorpayOrderId(
        sendCartDataRequest: SendCartDataRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    )

    fun placeRazorpayOrder(
        placeRazorpayOrderRequest: PlaceRazorpayOrderRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    )

    fun getMyOrderList(
        onSuccess: OnSuccess<MyOrderListResponse>,
        onError: OnError<Any>
    )

    fun checkCoupon(
        request: String,
        onSuccess: OnSuccess<MyOrderListResponse>,
        onError: OnError<Any>
    )

    fun applyCouponCode(
        request: GetCouponDetailsRequest,
        onSuccess: OnSuccess<GetCouponDetailsResponse>,
        onError: OnError<Any>
    )

    fun getCouponList(
        request: CouponListRequest,
        onSuccess: OnSuccess<GetCouponListResponse>,
        onError: OnError<Any>
    )


    companion object : Provider<ICheckOutRepository>() {
        override fun create(args: Array<out Any>): CheckOutRepository {
            if (args.size != 3) throw IllegalArgumentException("args size must be 4")

            val context = if (args[0] !is Context)
                throw IllegalArgumentException("args[0] is not Context")
            else
                args[0] as Context

            val api = if (args[1] !is ICheckOutApi)
                throw IllegalArgumentException("args[1] is not ArticleDao")
            else
                args[1] as ICheckOutApi

            val iPref = if (args[2] !is IPreferenceManager)
                throw IllegalArgumentException("args[2] is not IAPIWelcome")
            else
                args[2] as IPreferenceManager

            return CheckOutRepository(context, api, iPref)
        }
    }
}