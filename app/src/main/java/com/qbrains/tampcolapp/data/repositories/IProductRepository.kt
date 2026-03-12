package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.qbrains.tampcolapp.data.network.api.IProductApi
import com.qbrains.tampcolapp.data.network.api.reponse.CartListResponse
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.IndividualProductResponse2
import com.qbrains.tampcolapp.data.network.api.reponse.ProductListResponse
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.utility.Provider
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess

interface IProductRepository {

    fun getProductDetails(
        productDetailRequest: ProductDetailRequest,
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    )

    fun getCartList(
        cartListRequest: CartListRequest,
        onSuccess: OnSuccess<CartListResponse>,
        onError: OnError<Any>
    )

    fun updateCart(
        updateCartRequest: UpdateCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    )

    fun removeCart(
        removeCartRequest: RemoveCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    )

    fun addToCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    )

    fun getIndividualProduct(
        productDetailRequest: IndividualProductRequest,
        onSuccess: OnSuccess<IndividualProductResponse2>,
        onError: OnError<Any>
    )

    fun getServiceQueryResult(
        query:String,
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    )

    companion object : Provider<IProductRepository>() {
        override fun create(args: Array<out Any>): IProductRepository {
            if (args.size != 3) throw IllegalArgumentException("args size must be 4")

            val context = if (args[0] !is Context)
                throw IllegalArgumentException("args[0] is not Context")
            else
                args[0] as Context

            val api = if (args[1] !is IProductApi)
                throw IllegalArgumentException("args[1] is not ArticleDao")
            else
                args[1] as IProductApi

            val iPref = if (args[2] !is IPreferenceManager)
                throw IllegalArgumentException("args[2] is not IAPIWelcome")
            else
                args[2] as IPreferenceManager

            return ProductRepository(context, api, iPref)
        }
    }
}