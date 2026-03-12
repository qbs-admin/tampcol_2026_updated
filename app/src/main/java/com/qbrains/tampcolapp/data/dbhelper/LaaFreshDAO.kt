package com.qbrains.tampcolapp.data.dbhelper

import androidx.lifecycle.LiveData
import androidx.room.*
import com.qbrains.tampcolapp.data.dbhelper.table.*


@Dao
interface LaaFreshDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubCategoriesItem(subCategoriesItem: List<SubCategoriesItemEntity>)

    @Query("select * from SubCategoriesItemEntity")
    fun getSubCategoriesItem(): List<SubCategoriesItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoriesItem(CategoriesItem: List<CategoriesItemEntity>)

    @Query("select * from CategoriesItemEntity")
    fun getCategoriesItem(): List<CategoriesItemEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductItems(productItems: List<ProductPageEntity>)

    @Query("select * from ProductPageEntity")
    fun getProductItems(): List<ProductPageEntity>

    @Query("select * from ProductPageEntity where ProductPageEntity.product_id=:productId")
    fun getProductItemsDetails(productId: String): List<ProductPageEntity>

    @Insert
    fun insertUserLogin(loginEntity: LoginEntity)

    @Query("select * from LoginEntity")
    fun getUserDetail(): LoginEntity

    @Query("delete from LoginEntity")
    fun deleteUserDetail()


    @Query("delete from ProductPageEntity")
    fun deleteAllProducts()

    @Query("delete from ProductEntity")
    fun deleteAllProduct()

    @Insert
    fun insertProductCategory(categoryList: List<ProductCategoryEntity>)

    @Query("delete from ProductCategoryEntity")
    fun deleteAllProductCategory()

    @Query("select * from ProductEntity where ProductEntity.category=:categoryId")
    fun getProductByCategory(categoryId: String): List<ProductEntity>

    @Query("select * from ProductEntity where ProductEntity.category IN(:categoryId)")
    fun getProductByCategoryList(categoryId: List<String>): List<ProductEntity>

    @Query("select * from ProductEntity where ProductEntity.productId=:productCode")
    fun getProductByCode(productCode: String): ProductEntity

    @Query("select distinct * from ProductCategoryEntity")
    fun getProductCategory(): List<ProductCategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMyCard(myCartEntity: MyCartEntity)

    @Query("select * from  MyCartEntity where MyCartEntity.priceId=:productPriceId")
    fun getQtyAndQtyValue(productPriceId: String): MyCartEntity

    @Query("select * from MyCartEntity where MyCartEntity.user_id=:userCode ")
    fun getMyCardList(userCode: String): List<MyCartEntity>

    @Query("select * from MyCartEntity")
    fun getMyCardList(): List<MyCartEntity>

    @Query("SELECT orderQty FROM MyCartEntity where MyCartEntity.priceId=:productPriceId")
    fun getCartCount(productPriceId: String): Int

     @Query("SELECT COUNT(*) FROM MyCartEntity where MyCartEntity.productCode=:productCode")
    fun getCartCountProductcode(productCode: String): Int

    @Query("SELECT COUNT(*) FROM MyCartEntity")
    fun getAllCartCount(): LiveData<Int>

    @Query("select * from MyCartEntity where MyCartEntity.user_id=:userCode and MyCartEntity.productCode=:productCode and MyCartEntity.productWight=:unit")
    fun getMyCardByProductCode(userCode: String, productCode: String, unit: String): MyCartEntity


    @Query("update MyCartEntity set discountAmount=:discountAmount,subtotal=:subtotal,tax=:tax,orderValue=:orderValue where MyCartEntity.user_id=:userCode and MyCartEntity.productCode=:productCode and MyCartEntity.productWight=:unit")
    fun updateMyCard(
        userCode: String,
        productCode: String,
        unit: String,
        discountAmount: Double,
        subtotal: Double,
        tax: Double,
        orderValue: Double
    )
    @Transaction
    @Query("update MyCartEntity set orderQty=:qty,orderValue=:qtyValue where MyCartEntity.priceId=:priceId")
    fun updateMyCart(
        qty:Int,
        qtyValue:Double,
        priceId: String
    )

    @Query("SELECT SUM(orderValue  +(discountAmount * orderQty)) FROM MyCartEntity")
    fun getSubTotal(): Int

    @Query("SELECT SUM(discountAmount * orderQty) FROM MyCartEntity")
    fun getDiscountTotal(): Int

    @Query( "SELECT SUM(orderValue) FROM MyCartEntity" )
    fun getTotal():  Double

    @Transaction
    @Query("delete from MyCartEntity where MyCartEntity.priceId=:productPriceId")
    fun deleteMyCardItem(productPriceId: String)

    @Query("delete from MyCartEntity")
    fun deleteAllMyCardItem()

    @Query("select sum(MyCartEntity.orderValue) from MyCartEntity where MyCartEntity.user_id=:userCode")
    fun getMyOrderAmount(userCode: String): Double


    @Query("select sum(MyCartEntity.discountAmount) from MyCartEntity where MyCartEntity.user_id=:userCode")
    fun getMyOrderDiscountAmount(userCode: String): Double

    @Query("select * from MyCartEntity where MyCartEntity.priceId=:productPriceId")
    fun checkItemAlreadyExist(
        productPriceId: String
    ): MyCartEntity

    @Insert
    fun insertDeliveryAddress(deliveryAddressList: List<DeliveryAddressEntity>)

    @Query("select * from DeliveryAddressEntity")
    fun getDeliveryAddress(): List<DeliveryAddressEntity>

    @Query("delete from DeliveryAddressEntity")
    fun deleteAllDeliveryAddress()

    @Insert
    fun insertProductPriceDetails(priceList: List<ProductPriceEntity>)

    @Query("select * from ProductPriceEntity")
    fun getProductPriceList(): List<ProductPriceEntity>

    @Query("select * from ProductPriceEntity where ProductPriceEntity.product_id= :productCode")
    fun getProductPriceListByProductCode(productCode: String): List<ProductPriceEntity>

    @Query("select * from ProductPriceEntity where ProductPriceEntity.product_id= :productCode and ProductPriceEntity.id= :priceID  and ProductPriceEntity.unit= :unit")
    fun getProductPriceByPriceID(
        productCode: String,
        priceID: String,
        unit: String
    ): ProductPriceEntity

    @Query("delete from ProductPriceEntity")
    fun deleteProductPriceDetails()

}