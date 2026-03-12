package com.qbrains.tampcolapp.data.network.api.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class  ProductListRequest{

    @SerializedName("subcategory_id")
    @Expose
//    var categoryId : String?=null
    var product_id : String?=null

}
class  ProductDetailRequest{

    @SerializedName("category_id")
    @Expose
    var product_id : String?=null

}