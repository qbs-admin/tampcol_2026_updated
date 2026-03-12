package com.qbrains.tampcolapp.data.network.api

import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface IUserApi {

    @POST(LOG_IN)
    fun userLogin(
        @Body loginRequest: LoginRequest
    ): Deferred<Response<LoginResponse>>

    @POST(FACEBOOK_LOGIN)
    fun facebookLoginAsync(
        @Body loginRequest: SocialLoginRequest
    ): Deferred<Response<LoginResponse>>

    @POST(GOOGLE_LOGIN)
    fun googleLoginAsync(
        @Body loginRequest: SocialLoginRequest
    ): Deferred<Response<LoginResponse>>

    @POST("v4/token")
    fun googleAuthTokenAsync(
        @Body googleAuthRequest: GoogleAuthRequest
    ): Deferred<Response<GoogleAuthResponse>>

    @POST(OTP_LOG_IN)
    fun loginwithOtp(
        @Body loginOtpRequest: loginwithOtpRequest
    ): Deferred<Response<OTPResponse>>

    @POST(OTP_VERIFY)
    fun verifyOtp(
        @Body verifyOtpRequest: verifyOtpRequest
    ): Deferred<Response<LoginResponse>>


    @POST(SUB_CAT_ITEM)
    fun getSubCategories(): Deferred<Response<SubCategoriesResponse>>

    @GET(CATEGORIE)
    fun getCategories(): Deferred<Response<CategoriesResponse>>

    @GET(HOME)
    fun getHomepageData(): Deferred<Response<HomePageResponse>>

    @GET(SLIDES)
    fun getSlides(): Deferred<Response<SlidersResponse>>

    @POST(AD1)
    fun getad1(): Deferred<Response<SlidersResponse>>

    @POST(BUNDLE)
    fun getProductBundle(): Deferred<Response<ProductBundleResponse>>

    @GET(MINI_ORDER)
    fun getMiniOrder(): Deferred<Response<MiniOrderResponse>>

    @GET(APP_VERSION)
    fun checkVersion(): Deferred<Response<versionResponse>>

    @POST(IProductApi.ADD_CART)
    fun addCartAsync(
        @Body addCartRequest: AddCartRequest
    ): Deferred<Response<ErrorResMessage>>

    @POST(IProductApi.CART_LIST)
    fun getCartListAsync(@Body cartListRequest: CartListRequest): Deferred<Response<CartListResponse>>


    companion object {
        const val LOG_IN: String = "login"
        const val GOOGLE_LOGIN: String = "google-login"
        const val FACEBOOK_LOGIN: String = "facebook-login"
        const val OTP_LOG_IN: String = "request-otp"
        const val OTP_VERIFY: String = "verify-otp"
        const val SUB_CAT_ITEM: String = "subcategories"
        const val SLIDES: String = "slider"
        const val BUNDLE: String = "product_bundle"
        const val MINI_ORDER: String = "minimum-order-amt"
        const val AD1: String = "ad.php"

        //        const val CATEGORIE: String = "products.php"
        const val CATEGORIE: String = "category"
        const val HOME: String = "home"
        const val APP_VERSION: String = "version"

    }
}