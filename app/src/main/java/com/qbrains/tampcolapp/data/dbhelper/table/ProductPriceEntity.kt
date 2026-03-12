package com.qbrains.tampcolapp.data.dbhelper.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.NonNull

@Entity
class ProductPriceEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "serialNumber")
    var sno: Int = 0
    @ColumnInfo(name = "id")
    private var id: String? = null
    @ColumnInfo(name = "product_id")
    private var productId: String? = null
    @ColumnInfo(name = "unit")
    private var unit: String? = null
    @ColumnInfo(name = "sale_price")
    private var salePrice: String? = null
    @ColumnInfo(name = "purchase_price")
    private var purchasePrice: String? = null
    @ColumnInfo(name = "shipping_cost")
    private var shippingCost: String? = null
    @ColumnInfo(name = "discount")
    private var discount: String? = null
    @ColumnInfo(name = "discount_type")
    private var discountType: String? = null
    @ColumnInfo(name = "tax")
    private var tax: String? = null
    @ColumnInfo(name = "tax_type")
    private var taxType: String? = null

//    fun getSno(): Int? {
//        return sno
//    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getProductId(): String? {
        return productId
    }

    fun setProductId(productId: String) {
        this.productId = productId
    }

    fun getUnit(): String? {
        return unit
    }

    fun setUnit(unit: String) {
        this.unit = unit
    }

    fun getSalePrice(): String? {
        return salePrice
    }

    fun setSalePrice(salePrice: String) {
        this.salePrice = salePrice
    }

    fun getPurchasePrice(): String? {
        return purchasePrice
    }

    fun setPurchasePrice(purchasePrice: String) {
        this.purchasePrice = purchasePrice
    }

    fun getShippingCost(): String? {
        return shippingCost
    }

    fun setShippingCost(shippingCost: String) {
        this.shippingCost = shippingCost
    }

    fun getDiscount(): String? {
        return discount
    }

    fun setDiscount(discount: String) {
        this.discount = discount
    }

    fun getDiscountType(): String? {
        return discountType
    }

    fun setDiscountType(discountType: String) {
        this.discountType = discountType
    }

    fun getTax(): String? {
        return tax
    }

    fun setTax(tax: String) {
        this.tax = tax
    }

    fun getTaxType(): String? {
        return taxType
    }

    fun setTaxType(taxType: String) {
        this.taxType = taxType
    }

}
