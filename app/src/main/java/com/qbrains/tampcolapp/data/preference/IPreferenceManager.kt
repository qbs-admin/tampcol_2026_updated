package com.qbrains.tampcolapp.data.preference

interface IPreferenceManager {

    fun saveToken(token: String?)
    fun getToken(): String

    fun setIsLoggedIn(status: Boolean)
    fun getIsLoggedIn(): Boolean

    fun setIsStoreSelected(status: Boolean)
    fun getIsStoreSelected(): Boolean

    fun setCustomerId(customerId:String)
    fun getCustomerId(): String

    fun setCartCout(customerId:String)
    fun getCartCout(): String

    fun setDeviceToken(deviceToken:String)
    fun getDeviceToken(): String

    fun setFirebaseToken(firebaseToken:String)
    fun getFirebaseToken(): String

    fun setUserPhoneNumber(PhoneNumber:String)
    fun getUserPhoneNumber(): String

    fun setUserEmailId(PhoneNumber:String)
    fun getUserEmailId(): String

    fun setMiniAmount(miniAmount:String)
    fun getMiniAmount(): String

    fun setDeliveryType(deliveryType:String)
    fun getDeliveryType(): String

    fun setDateSelected(DateSelected:String)
    fun getDateSelected(): String

    fun setTimeSelected(TimeSelected:String)
    fun getTimeSelected(): String


    fun setSelectedStore(storeSelected:String)
    fun getSelectedStore(): String

    fun setHomeViewType(ViewType:Int)
    fun getHomeViewType(): Int
}