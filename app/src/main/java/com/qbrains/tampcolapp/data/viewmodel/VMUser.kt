package com.qbrains.tampcolapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.network.api.GoogleAuthRequest
import com.qbrains.tampcolapp.data.network.api.GoogleAuthResponse
import com.qbrains.tampcolapp.data.network.api.IUserApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.network.manager.NetworkingManager
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.repositories.IUserRepository
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import org.jetbrains.annotations.NotNull

class VMUser(
    private val context: Context,
    @NotNull private val repository: IUserRepository,
) :
    ViewModel() {

    fun userLogin(
        emailId: String, password: String,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
//        onError: OnError<LoginResponse>
    ) {
        val reqLogin = LoginRequest()
        reqLogin.email = emailId
        reqLogin.password = password
        repository.userLogin(reqLogin, onSuccess, onError)
    }

    fun googleLogin(
        accessToken: String,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    ) {
        val reqLogin = SocialLoginRequest()
        reqLogin.accessToken = accessToken
        repository.googleLogin(reqLogin, onSuccess, onError)
    }

    fun facebookLogin(
        accessToken: String,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    ) {
        val reqLogin = SocialLoginRequest()
        reqLogin.accessToken = accessToken
        repository.facebookLogin(reqLogin, onSuccess, onError)
    }

    fun googleJWTToken(
        accessToken: String,
        onSuccess: OnSuccess<GoogleAuthResponse>,
        onError: OnError<Any>
    ) {
        val reqLogin = GoogleAuthRequest(
            "authorization_code",
            "635032242401-vldu6o2nqka9jl2ceiiv4f8g44uo1gen.apps.googleusercontent.com",
            "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",
            "",
            accessToken
        )
        repository.googleJWTToken(reqLogin, onSuccess, onError)
    }

    fun getSubCategories(
        onSuccess: OnSuccess<SubCategoriesResponse>,
        onError: OnError<Any>
    ) {
        repository.getSubCategories(onSuccess, onError)
    }

    fun getCategories(
        onSuccess: OnSuccess<CategoriesResponse>,
        onError: OnError<Any>
    ) {
        repository.getCategories(onSuccess, onError)
    }

    fun getHomepageData(
        onSuccess: OnSuccess<HomePageResponse>,
        onError: OnError<Any>
    ) {
        repository.getHomepageData(onSuccess, onError)
    }

    fun checkVersion(
        onSuccess: OnSuccess<versionResponse>,
        onError: OnError<Any>
    ) {
        repository.checkVersion(onSuccess, onError)
    }

//    fun getAds(
//        onSuccess: OnSuccess<SubCategoriesResponse>,
//        onError: OnError<Any>
//    ) {
//        repository.getSubCategories(onSuccess, onError)
//    }

    fun getSlides(
        onSuccess: OnSuccess<SlidersResponse>,
        onError: OnError<Any>
    ) {
        repository.getSlides(onSuccess, onError)
    }

    fun getad1(
        onSuccess: OnSuccess<SlidersResponse>,
        onError: OnError<Any>
    ) {
        repository.getSlides(onSuccess, onError)
    }

    fun getProductBundle(
        onSuccess: OnSuccess<ProductBundleResponse>,
        onError: OnError<Any>
    ) {
        repository.getProductBundle(onSuccess, onError)
    }

    fun loginViaOtp(
        phone: String,
        onSuccess: OnSuccess<OTPResponse>,
        onError: OnError<Any>
    ) {
        val reqOtp = loginwithOtpRequest()
        reqOtp.phone = phone
        repository.loginwithOtp(reqOtp, onSuccess, onError)
    }

    fun verifyOtp(
        otp: String, hash: String,
        onSuccess: OnSuccess<LoginResponse>,
        onError: OnError<Any>
    ) {
        val req = verifyOtpRequest()
        req.otp = otp
        req.otp_hash = hash
        repository.verifyOtp(req, onSuccess, onError)
    }


    fun getMiniOrder(onSuccess: OnSuccess<MiniOrderResponse>, onError: OnError<Any>) {
        repository.getMiniOrder(onSuccess, onError)
    }

    fun addCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        repository.addToCart(addCartRequest, onSuccess, onError)
    }

    fun getCartList(
        cartListRequest: CartListRequest,
        onSuccess: OnSuccess<CartListResponse>,
        onError: OnError<Any>
    ) {

        repository.getCartList(cartListRequest, onSuccess, onError)
    }




    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {

        private val repository = IUserRepository.get(
            context,
//            database,
            NetworkingManager.createApi<IUserApi>(context),
            PreferenceManager(context)
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return VMUser(context, repository) as T
        }
    }
}