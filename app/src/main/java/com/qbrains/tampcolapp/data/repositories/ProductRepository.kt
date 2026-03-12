package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.qbrains.tampcolapp.data.network.api.IProductApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import kotlinx.coroutines.*

class ProductRepository(
    private val context: Context,
    private val iProductApi: IProductApi,
    public val iPref: IPreferenceManager
) : IProductRepository {
    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.IO + mJob)

    override fun getProductDetails(
        productDetailRequest: ProductDetailRequest,
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iProductApi.getProductDetails(productDetailRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {

//                        iPref.setIsLoggedIn(true)
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

    override fun getCartList(
        cartListRequest: CartListRequest,
        onSuccess: OnSuccess<CartListResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iProductApi.getCartListAsync(cartListRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {

//                        iPref.setIsLoggedIn(true)
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

    override fun updateCart(
        updateCartRequest: UpdateCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iProductApi.updateCartAsync(updateCartRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {

//                        iPref.setIsLoggedIn(true)
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

    override fun removeCart(
        removeCartRequest: RemoveCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iProductApi.removeCartAsync(removeCartRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {

//                        iPref.setIsLoggedIn(true)
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

    override fun addToCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iProductApi.addCartAsync(addCartRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {

//                        iPref.setIsLoggedIn(true)
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

    override fun getIndividualProduct(
        productDetailRequest: IndividualProductRequest,
        onSuccess: OnSuccess<IndividualProductResponse2>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iProductApi.getIndividualProduct(productDetailRequest).await()
                if (response.isSuccessful) {
                    response.body()?.let {


//                        iPref.setIsLoggedIn(true)
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

    override fun getServiceQueryResult(
        query: String,
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val jsonReq = JsonObject()
                jsonReq.addProperty("title", query)
//                jsonReq.addProperty("user_type", iPref.getUserType())
                val response = iProductApi.getQueryResultAsync(jsonReq).await()
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