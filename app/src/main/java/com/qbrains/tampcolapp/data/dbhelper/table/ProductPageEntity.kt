package com.qbrains.tampcolapp.data.dbhelper.table

import androidx.room.*
import com.google.gson.Gson
import com.qbrains.tampcolapp.data.network.api.reponse.varient

@Entity(indices = [Index(value = ["product_id"], unique = true)])
data class ProductPageEntity(
    @PrimaryKey(autoGenerate = true) var product_id: Int,
    var title: String,
    var category: String,
    var description: String,
    var subCategory: String,
    var deal: String,
    var pricevariant: List<varient>
)


//@Entity(indices = [Index(value = ["productId"], unique = true)])
//@Entity
//class PriceVarientEntity {
////    @PrimaryKey(autoGenerate = true)
//    var product_weight: String = ""
//    var price: String = ""
//    var image: String = ""
//    var stock: String = ""
//    var discount: String = ""
//
//
//
//
//
//    constructor(product_weight: String, price: String,
//                image: String, stock: String, discount: String) {
//        this.product_weight = product_weight
//        this.price = price
//        this.image = image
//        this.stock = stock
//        this.discount = discount
//    }
//}

class Converters {

    @TypeConverter
    fun listToJsonString(value: List<varient>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<varient>::class.java).toList()
}


//    {
//        "products": [
//        {
//
//            "description": "Harina Gram Dhall is a collection of selectively picked gram grains that are high in nutrients. Gram Dhall or split moong dhall is one of the main pulses used in every Indian household to cook delicious side and main dishes like khichdi, vada, soup, stuffing, etc. ",
//            "sub_category": "219",
//            "deal": "no",
//            "price_variant": [
//