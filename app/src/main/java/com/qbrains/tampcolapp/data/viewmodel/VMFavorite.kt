package com.qbrains.tampcolapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.network.api.IFavoriteApi
import com.qbrains.tampcolapp.data.network.api.reponse.AddWishResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.ProductListResponse
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.network.manager.NetworkingManager
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.repositories.IFavoriteRepository
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import org.jetbrains.annotations.NotNull

class VMFavorite(
    private val context: Context,
    @NotNull private val repository: IFavoriteRepository
) :
    ViewModel() {

    fun getFavorite(
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    ) {
        repository.getFavorite( onSuccess, onError)
    }
//    fun getFavorite(
//        onSuccess: OnSuccess<GetWishListResponse>,
//        onError: OnError<Any>
//    ) {
//        repository.getFavorite( onSuccess, onError)
//    }

    fun addtoWishlist(
        productId:String,
        onSuccess: OnSuccess<AddWishResMessage>,
        onError: OnError<Any>
    ){
//        val request=AddWishListRequest()
//        request.productId=productId

        repository.addFavorite(productId,onSuccess,onError)
    }

    fun addCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        repository.addToCart(addCartRequest, onSuccess, onError)
    }

    fun delfromWishlist(
        productId:String,
        onSuccess: OnSuccess<AddWishResMessage>,
        onError: OnError<Any>
    ){
        repository.delFavorite(productId,onSuccess,onError)
    }

    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {


        private val repository = IFavoriteRepository.get(
            context,
//            database,
            NetworkingManager.createApi<IFavoriteApi>(context),
            PreferenceManager(context)
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return VMFavorite(context, repository) as T
        }
    }
}