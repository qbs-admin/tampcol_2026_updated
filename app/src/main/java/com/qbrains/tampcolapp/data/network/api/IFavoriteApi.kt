package com.qbrains.tampcolapp.data.network.api

import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.network.api.request.AddWishListRequest
import com.qbrains.tampcolapp.data.network.api.request.GetWishListRequest
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface IFavoriteApi {

    @POST(GET_WISH_LISTS)
    fun getFavorite(
        @Body getWishListRequest: GetWishListRequest
    ): Deferred<Response<ProductListResponse>>

  @POST(DEL_WISH_LISTS)
    fun delFavorite(
        @Body getWishListRequest: AddWishListRequest
    ): Deferred<Response<AddWishResMessage>>

    @POST(ADD_WISH_LISTS)
    fun addFavorite(
        @Body getWishListRequest: AddWishListRequest
    ): Deferred<Response<AddWishResMessage>>

    @POST(IProductApi.ADD_CART)
    fun addCartAsync(
        @Body addCartRequest: AddCartRequest
    ): Deferred<Response<ErrorResMessage>>

    companion object {
        const val GET_WISH_LISTS: String = "wishlist"
        const val ADD_WISH_LISTS: String = "add-wishlist"
        const val DEL_WISH_LISTS: String = "remove-wishlist"

    }
}