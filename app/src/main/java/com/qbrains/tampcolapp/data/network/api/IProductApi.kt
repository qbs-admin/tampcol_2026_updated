package com.qbrains.tampcolapp.data.network.api

import com.google.gson.JsonObject
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IProductApi {
    @POST(GET_PRODUCT_DETAIL)
    fun getProductDetails(@Body request: ProductDetailRequest): Deferred<Response<ProductListResponse>>

    @POST(CART_LIST)
    fun getCartListAsync(@Body cartListRequest: CartListRequest): Deferred<Response<CartListResponse>>

    @POST(PRODUCT)
    fun getProduct(@Body request: ProductDetailRequest): Deferred<Response<GetProductListResponse>>

    @POST(GET_PRODUCT)
    fun getIndividualProduct(@Body request: IndividualProductRequest): Deferred<Response<IndividualProductResponse2>>

    @POST(SEARCH_PRODUCTS)
    fun getQueryResultAsync(@Body request: JsonObject): Deferred<Response<ProductListResponse>>

    @POST(ADD_CART)
    fun addCartAsync(
        @Body addCartRequest: AddCartRequest
    ): Deferred<Response<ErrorResMessage>>

    @POST(UPDATE_CART)
    fun updateCartAsync(
        @Body updateCartRequest: UpdateCartRequest
    ): Deferred<Response<ErrorResMessage>>

    @POST(REMOVE_CART)
    fun removeCartAsync(
        @Body removeCartRequest: RemoveCartRequest
    ): Deferred<Response<ErrorResMessage>>


    companion object {
        const val GET_PRODUCT_DETAIL: String = "products"
        //        const val GET_PRODUCT_DETAIL: String = "products.php"
        const val GET_PRODUCT: String = "product-detail"
        const val PRODUCT: String = "products"
        const val ADD_CART: String = "cart/add"
        const val CART_LIST: String = "cart/list"
        const val UPDATE_CART: String = "cart/update"
        const val REMOVE_CART: String = "cart/remove"
        const val SEARCH_PRODUCTS: String = "search"
    }
}
//TODO(onclick of rv productid to category id and pass into getproductlist.php)