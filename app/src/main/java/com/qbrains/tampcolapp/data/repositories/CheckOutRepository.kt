package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.google.gson.Gson
import com.qbrains.tampcolapp.data.network.api.ICheckOutApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import kotlinx.coroutines.*

class CheckOutRepository(
    private val context: Context,
    private val iCheckOurApi: ICheckOutApi,
    private val iPref: IPreferenceManager
) : ICheckOutRepository {


    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.IO + mJob)
    override fun getDefaultAddress(
        onSuccess: OnSuccess<GetDefaultAddressResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val getWishListRequest = GetWishListRequest()
                getWishListRequest.userId = iPref.getCustomerId()
                val response = iCheckOurApi.getdefaultaddress(getWishListRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun placingOrders(
        sendCartDataRequest: SendCartDataRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val getWishListRequest = GetWishListRequest()
                getWishListRequest.userId = iPref.getCustomerId()
                val response = iCheckOurApi.placingOrders(sendCartDataRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun createRazorpayOrderId(
        sendCartDataRequest: SendCartDataRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val getWishListRequest = GetWishListRequest()
                getWishListRequest.userId = iPref.getCustomerId()
                val response = iCheckOurApi.createRazorpayOrderIdAsync(sendCartDataRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun placeRazorpayOrder(
        placeRazorpayOrderRequest: PlaceRazorpayOrderRequest,
        onSuccess: OnSuccess<SalesResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val getWishListRequest = GetWishListRequest()
                getWishListRequest.userId = iPref.getCustomerId()
                val response = iCheckOurApi.placeRazorpayOrder(placeRazorpayOrderRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun getMyOrderList(
        onSuccess: OnSuccess<MyOrderListResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val myOrderRequest = MyOrderRequest()
                myOrderRequest.id = iPref.getCustomerId()
                val response = iCheckOurApi.getMyOrderList(myOrderRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun checkCoupon(
        request: String,
        onSuccess: OnSuccess<MyOrderListResponse>,
        onError: OnError<Any>
    ) {
//        abc TODO
        mScope.launch {
            try {
                val myOrderRequest = MyOrderRequest()
                myOrderRequest.id = iPref.getCustomerId()
                val response = iCheckOurApi.getMyOrderList(myOrderRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun applyCouponCode(
        request: GetCouponDetailsRequest,
        onSuccess: OnSuccess<GetCouponDetailsResponse>,
        onError: OnError<Any>
    ) {

        mScope.launch {
            try {
//                val myOrderRequest = MyOrderRequest()
                request.userId = iPref.getCustomerId().toInt()
                val response = iCheckOurApi.applyCoupon(request).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }

    override fun getCouponList(
        request: CouponListRequest,
        onSuccess: OnSuccess<GetCouponListResponse>,
        onError: OnError<Any>
    ) {

        mScope.launch {
            try {


                val response = iCheckOurApi.getCouponList(request).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) { onSuccess(it) }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ResMessage::class.java
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                }

            }
        }
    }


}