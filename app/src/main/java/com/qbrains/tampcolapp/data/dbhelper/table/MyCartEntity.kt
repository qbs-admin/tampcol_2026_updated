package com.qbrains.tampcolapp.data.dbhelper.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["priceId"], unique = true)])
class MyCartEntity {

    @PrimaryKey(autoGenerate = true)
    var sno: Int = 0
    var productCode: String = ""
    var productName: String = ""
    var orderQty: Int = 0
    var orderValue: Double = 0.0
    var productWight: String = ""
    var productURI: String = ""
    var priceId: String = ""
    var user_id: String = ""
    var discountAmount: Double = 0.0
    var subtotal: Double = 0.0
    var tax: Double = 0.0

    constructor(
        productCode: String, productName: String, orderQty: Int, orderValue: Double,
        productWight: String, productURI: String, priceId: String, user_id: String,
        discountAmount: Double, subtotal: Double, tax: Double) {
        this.productCode = productCode
        this.productName = productName
        this.orderQty = orderQty
        this.orderValue = orderValue
        this.productWight = productWight
        this.productURI = productURI
        this.priceId = priceId
        this.user_id = user_id
        this.discountAmount = discountAmount
        this.subtotal = subtotal
        this.tax = tax
    }
}