package com.qbrains.tampcolapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.network.api.ICheckOutApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.network.manager.NetworkingManager
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.repositories.ICheckOutRepository
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import org.jetbrains.annotations.NotNull

class VMCheckOut(
    private val context: Context,
    @NotNull private val repository: ICheckOutRepository
) : ViewModel() {
    fun getDefaultAddress(
        onSuccess: OnSuccess<GetDefaultAddressResponse>,
        onError: OnError<Any>
    ) {
        repository.getDefaultAddress(onSuccess, onError)
    }

    fun placingOrders(
        sendCartDataRequest: SendCartDataRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    ) {
        repository.placingOrders(sendCartDataRequest, onSuccess, onError)
    }

    fun createRazorpayOrderId(
        sendCartDataRequest: SendCartDataRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    ) {
        repository.createRazorpayOrderId(sendCartDataRequest, onSuccess, onError)
    }

    fun placeRazorpayOrder(
        placeRazorpayOrderRequest: PlaceRazorpayOrderRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    ) {
        repository.placeRazorpayOrder(placeRazorpayOrderRequest, onSuccess, onError)
    }

    fun getMyOrderList(
        onSuccess: OnSuccess<MyOrderListResponse>,
        onError: OnError<Any>
    ) {
        repository.getMyOrderList(onSuccess, onError)
    }

//  fun checkCouponCode(
//        request: String,
//        onSuccess: OnSuccess<MyOrderListResponse>,
//        onError: OnError<Any>
//    ){
//        repository.checkCoupon(request,onSuccess, onError)
////      TODO aa
//    }

    fun applyCouponCode(
        request: GetCouponDetailsRequest,
        onSuccess: OnSuccess<GetCouponDetailsResponse>,
        onError: OnError<Any>
    ) {
        repository.applyCouponCode(request, onSuccess, onError)
    }

    fun getCouponList(
        request: CouponListRequest,
        onSuccess: OnSuccess<GetCouponListResponse>,
        onError: OnError<Any>
    ) {
        repository.getCouponList(request, onSuccess, onError)
    }


    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {
        private val repository = ICheckOutRepository.get(
            context,
//            database,
            NetworkingManager.createApi<ICheckOutApi>(context),
            PreferenceManager(context)
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return VMCheckOut(context, repository) as T
        }
    }


}