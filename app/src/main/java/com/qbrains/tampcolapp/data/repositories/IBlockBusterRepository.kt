package com.qbrains.tampcolapp.data.repositories

import android.content.Context
import com.qbrains.tampcolapp.data.network.api.IBlockBusterAPI
import com.qbrains.tampcolapp.data.network.api.reponse.ErrorResMessage
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.preference.IPreferenceManager
import com.qbrains.tampcolapp.data.utility.Provider
import com.qbrains.tampcolapp.ui.component.OnError
import com.qbrains.tampcolapp.ui.component.OnSuccess

interface IBlockBusterRepository {

    fun addToCart(
        addCartRequest: AddCartRequest,
        onSuccess: OnSuccess<ErrorResMessage>,
        onError: OnError<Any>
    )

    companion object : Provider<IBlockBusterRepository>() {
        override fun create(args: Array<out Any>): IBlockBusterRepository {
            if (args.size != 3) throw IllegalArgumentException("args size must be 4")

            val context = if (args[0] !is Context)
                throw IllegalArgumentException("args[0] is not Context")
            else
                args[0] as Context

            val api = if (args[1] !is IBlockBusterAPI)
                throw IllegalArgumentException("args[1] is not ArticleDao")
            else
                args[1] as IBlockBusterAPI

            val iPref = if (args[2] !is IPreferenceManager)
                throw IllegalArgumentException("args[2] is not IAPIWelcome")
            else
                args[2] as IPreferenceManager

            return BlockBusterRepository(context, api,  iPref)
        }
    }
}