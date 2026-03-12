package com.qbrains.tampcolapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.network.api.IProductApi
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.network.manager.NetworkingManager
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.repositories.IProductRepository
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess
import org.jetbrains.annotations.NotNull

class VMProduct(private val context: Context, @NotNull private val repository: IProductRepository) :
    ViewModel() {
    //    ,@NotNull private val wishRepo : IFavoriteRepository
    fun getProductDetails(
        product_id: String,
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    ) {
        val productListReq = ProductDetailRequest()
        productListReq.product_id = product_id
        repository.getProductDetails(productListReq, onSuccess, onError)
    }

    fun addCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {
        repository.addToCart(addCartRequest, onSuccess, onError)
    }

    fun getIndividualProduct(
        product_id: String,
        onSuccess: OnSuccess<IndividualProductResponse2>,
        onError: OnError<Any>
    ) {
        val individualProductRequest = IndividualProductRequest()
        individualProductRequest.product_id = product_id
        repository.getIndividualProduct(individualProductRequest, onSuccess, onError)
    }

    fun getServiceQueryResult(
        queryString: String,
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    ) {
        repository.getServiceQueryResult(queryString, onSuccess, onError)
    }

    /**
    fun addtoWishlist(
    request: AddWishListRequest,
    onSuccess: OnSuccess<AddWishResMessage>,
    onError: OnError<Any>
    ){
    //        val request=AddWishListRequest()
    //        request.productId=productId

    wishRepo.addFavorite(request,onSuccess,onError)
    }

     */

    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {


        private val repository = IProductRepository.get(
            context,
//            database,
            NetworkingManager.createApi<IProductApi>(context),
            PreferenceManager(context)
        )

        /** private val wishRepo = IFavoriteRepository.get(
        context,
        //            database,
        NetworkingManager.createApi<IProductApi>(context),
        PreferenceManager(context)
        )
         */

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return VMProduct(context, repository) as T
        }
    }
}