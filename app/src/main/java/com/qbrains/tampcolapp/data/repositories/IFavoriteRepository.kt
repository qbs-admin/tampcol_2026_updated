package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.qbrains.tampcolapp.data.network.api.IFavoriteApi
import com.qbrains.tampcolapp.data.network.api.reponse.AddWishResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.reponse.ProductListResponse
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.utility.Provider
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess

interface IFavoriteRepository {

    fun getFavorite(
        onSuccess: OnSuccess<ProductListResponse>,
        onError: OnError<Any>
    )

    fun addFavorite(
        productId: String,
        onSuccess: OnSuccess<AddWishResMessage>,
        onError: OnError<Any>
    )

    fun delFavorite(
        productId: String,
        onSuccess: OnSuccess<AddWishResMessage>,
        onError: OnError<Any>
    )

    fun addToCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    )

    companion object : Provider<IFavoriteRepository>() {
        override fun create(args: Array<out Any>): FavoriteRepository {
            if (args.size != 3) throw IllegalArgumentException("args size must be 4")

            val context = if (args[0] !is Context)
                throw IllegalArgumentException("args[0] is not Context")
            else
                args[0] as Context

            val api = if (args[1] !is IFavoriteApi)
                throw IllegalArgumentException("args[1] is not ArticleDao")
            else
                args[1] as IFavoriteApi

            val iPref = if (args[2] !is IPreferenceManager)
                throw IllegalArgumentException("args[2] is not IAPIWelcome")
            else
                args[2] as IPreferenceManager

            return FavoriteRepository(context, api,  iPref)
        }
    }
}