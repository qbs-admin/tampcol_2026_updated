package com.qbrains.tampcolapp.data.network.api

import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface IBlockBusterAPI {

    @POST(IProductApi.ADD_CART)
    fun addCartAsync(
        @Body addCartRequest: AddCartRequest
    ): Deferred<Response<ErrorResMessage>>

}