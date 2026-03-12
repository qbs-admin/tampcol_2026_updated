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

class VMCart(private val context: Context, @NotNull private val repository: IProductRepository) :
    ViewModel() {

    fun getCartList(
        cartListRequest: CartListRequest,
        onSuccess: OnSuccess<CartListResponse>,
        onError: OnError<Any>
    ) {

        repository.getCartList(cartListRequest, onSuccess, onError)
    }

    fun updateCart(
        updateCartRequest: UpdateCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {

        repository.updateCart(updateCartRequest, onSuccess, onError)
    }

    fun removeCart(
        removeCartRequest: RemoveCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    ) {

        repository.removeCart(removeCartRequest, onSuccess, onError)
    }


    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {

        private val repository = IProductRepository.get(
            context,
//            database,
            NetworkingManager.createApi<IProductApi>(context),
            PreferenceManager(context)
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return VMCart(context, repository) as T
        }
    }
}