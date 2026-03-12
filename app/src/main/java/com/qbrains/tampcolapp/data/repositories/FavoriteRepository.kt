package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.google.gson.Gson
import com.qbrains.tampcolapp.data.network.api.IFavoriteApi
import com.qbrains.tampcolapp.data.network.api.reponse.AddWishResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.ProductListResponse
import com.qbrains.tampcolapp.data.network.api.reponse.ResMessage
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.network.api.request.AddWishListRequest
import com.qbrains.tampcolapp.data.network.api.request.GetWishListRequest
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import kotlinx.coroutines.*

class FavoriteRepository(
    private val context: Context,
    private val iFavoriteApi: IFavoriteApi,
    private val iPref: IPreferenceManager
) : IFavoriteRepository {

    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.IO + mJob)

    override fun getFavorite(
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val getWishListRequest = GetWishListRequest()
                getWishListRequest.userId = iPref.getCustomerId()
                val response = iFavoriteApi.getFavorite(getWishListRequest).await()
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

    override fun addFavorite(
        productId: String,
        onSuccess: OnSuccess<AddWishResMessage>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val addWishListRequest = AddWishListRequest()
                addWishListRequest.productId=productId
                addWishListRequest.userId = iPref.getCustomerId()
                val response = iFavoriteApi.addFavorite(addWishListRequest).await()
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

    override fun delFavorite(
        productId: String,
        onSuccess: OnSuccess<AddWishResMessage>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val addWishListRequest = AddWishListRequest()
                addWishListRequest.productId=productId
                addWishListRequest.userId = iPref.getCustomerId()
                val response = iFavoriteApi.delFavorite(addWishListRequest).await()
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
                val response = iFavoriteApi.addCartAsync(addCartRequest).await()
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


}