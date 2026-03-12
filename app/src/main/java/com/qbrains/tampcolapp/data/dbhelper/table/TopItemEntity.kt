package com.qbrains.tampcolapp.data.dbhelper.table
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["product_id"], unique = true)])
class TopItemEntity {
    @PrimaryKey(autoGenerate = true)
    var product_id: Int = 0
    var title: String=""
    var price_variant: String?=null

    constructor(
        product_id: Int,
        title: String,
        price_variant: String,
    ) {
        this.product_id = product_id
        this.title = title
        this.price_variant = price_variant

    }
}

//@Entity(indices = [Index(value = ["product_id"], unique = true)])
//class TItemEntity {
//    @PrimaryKey(autoGenerate = true)
//    var product_weight: Int = 0
//    var price: String=""
//    var price_variant: String=""
//
//    constructor(
//        product_id: Int,
//        price: String,
//        price_variant: String,
//    ) {
//        this.product_id = product_id
//        this.title = title
//        this.price_variant = price_variant
//
//    }
//}