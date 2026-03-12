package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.google.gson.Gson
import android.util.Log
import com.qbrains.tampcolapp.data.network.api.GoogleAuthRequest
import com.qbrains.tampcolapp.data.network.api.GoogleAuthResponse
import com.qbrains.tampcolapp.data.network.api.IUserApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import kotlinx.coroutines.*

class UserRepository(
    private val context: Context,
    private val iUserApi: IUserApi,
    public val iPref: IPreferenceManager
) : IUserRepository {


    private val mJob = SupervisorJob()
    private val mScope = CoroutineScope(Dispatchers.IO + mJob)


    override fun userLogin(
        reqLogin: LoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iUserApi.userLogin(reqLogin).await()
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

    override fun googleLogin(
        reqLogin: SocialLoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iUserApi.googleLoginAsync(reqLogin).await()
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

    override fun facebookLogin(
        reqLogin: SocialLoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iUserApi.facebookLoginAsync(reqLogin).await()
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

    override fun googleJWTToken(
        requestToken: GoogleAuthRequest,
        onSuccess: OnSuccess<GoogleAuthResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iUserApi.googleAuthTokenAsync(requestToken).await()
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

    override fun loginwithOtp(
        req: loginwithOtpRequest,
        onSuccess: OnSuccess<OTPResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iUserApi.loginwithOtp(req).await()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("TAG", "Login button clicked");
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

    override fun verifyOtp(
        req: verifyOtpRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {

            try {
                Log.d("TAG", "Login OTP");
                val response = iUserApi.verifyOtp(req).await()
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
                    onError("Something went wrong")
                }

            }
        }
    }

    override fun getSubCategories(
        onSuccess: OnSuccess<SubCategoriesResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iUserApi.getSubCategories().await()
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

    override fun getCategories(onSuccess: OnSuccess<CategoriesResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val response = iUserApi.getCategories().await()
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

    override fun getHomepageData(onSuccess: OnSuccess<HomePageResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val response = iUserApi.getHomepageData().await()
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

    override fun getSlides(onSuccess: OnSuccess<SlidersResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val response = iUserApi.getSlides().await()
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

    override fun getad1(onSuccess: OnSuccess<SlidersResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val response = iUserApi.getad1().await()
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

    override fun checkVersion(onSuccess: OnSuccess<versionResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val response = iUserApi.checkVersion().await()
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

    override fun getProductBundle(
        onSuccess: OnSuccess<ProductBundleResponse>,
        onError: OnError<Any>
    ) {
        mScope.launch {
            try {
                val response = iUserApi.getProductBundle().await()
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

    override fun getMiniOrder(onSuccess: OnSuccess<MiniOrderResponse>, onError: OnError<Any>) {
        mScope.launch {
            try {
                val response = iUserApi.getMiniOrder().await()
                if (response.isSuccessful) {
                    response.body()?.let {
                      iPref.setMiniAmount(it.orderMinAmount.toString())
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
                val response = iUserApi.addCartAsync(addCartRequest).await()
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
                val response = iUserApi.getCartListAsync(cartListRequest).await()
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