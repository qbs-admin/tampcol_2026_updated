package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.qbrains.tampcolapp.data.network.api.GoogleAuthRequest
import com.qbrains.tampcolapp.data.network.api.GoogleAuthResponse
import com.qbrains.tampcolapp.data.network.api.IUserApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.utility.Provider
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess

interface IUserRepository {

    fun userLogin(
        reqLogin: LoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    )

    fun googleLogin(
        reqLogin: SocialLoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    )

    fun facebookLogin(
        reqLogin: SocialLoginRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    )

    fun googleJWTToken(
        reqLogin: GoogleAuthRequest,
        onSuccess: OnSuccess<GoogleAuthResponse>,
        onError: OnError<Any>
    )

    fun loginwithOtp(
        req: loginwithOtpRequest,
        onSuccess: OnSuccess<OTPResponse>,
        onError: OnError<Any>
    )

    fun verifyOtp(
        req: verifyOtpRequest,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    )

    fun getSubCategories(
        onSuccess: OnSuccess<SubCategoriesResponse>,
        onError: OnError<Any>
    )
    fun getCategories(
        onSuccess: OnSuccess<CategoriesResponse>,
        onError: OnError<Any>
    )
     fun getHomepageData(
        onSuccess: OnSuccess<HomePageResponse>,
        onError: OnError<Any>
    )


    fun getSlides(
        onSuccess: OnSuccess<SlidersResponse>,
        onError: OnError<Any>
    )
    fun getad1(
        onSuccess: OnSuccess<SlidersResponse>,
        onError: OnError<Any>
    )
 fun checkVersion(
        onSuccess: OnSuccess<versionResponse>,
        onError: OnError<Any>
    )

    fun getProductBundle(
        onSuccess: OnSuccess<ProductBundleResponse>,
        onError: OnError<Any>
    )


    fun getMiniOrder(
        onSuccess: OnSuccess<MiniOrderResponse>,
        onError: OnError<Any>
    )

    fun addToCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    )

    fun getCartList(
        cartListRequest: CartListRequest,
        onSuccess: OnSuccess<CartListResponse>,
        onError: OnError<Any>
    )

    companion object : Provider<UserRepository>() {
        override fun create(args: Array<out Any>): UserRepository {
            if (args.size != 3) throw IllegalArgumentException("args size must be 4")

            val context = if (args[0] !is Context)
                throw IllegalArgumentException("args[0] is not Context")
            else
                args[0] as Context

            val api = if (args[1] !is IUserApi)
                throw IllegalArgumentException("args[1] is not ArticleDao")
            else
                args[1] as IUserApi

            val iPref = if (args[2] !is IPreferenceManager)
                throw IllegalArgumentException("args[2] is not IAPIWelcome")
            else
                args[2] as IPreferenceManager

            return UserRepository(context, api,  iPref)
        }
    }
}