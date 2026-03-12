package com.qbrains.tampcolapp.homemvc.retrofit

import java.util.regex.Pattern

//package com.jkapps.laafresh.homemvc.retrofit
//
//import com.jkapps.laafresh.MyApplication
//
//class Utils {
//    fun internetChack(): Boolean {
//        try {
//            val ConnectionManager =
//
//                MyApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val networkInfo = ConnectionManager.activeNetworkInfo
//            return networkInfo != null && networkInfo.isConnected
//        } catch (e: Exception) {
//            Log.e("Connectivity Exception", e.message)
//        }
//        return true
//    }
//    }

fun isValidMobile(phone: String) = phone.length == 10

fun isValidMail(email: String): Boolean {
    return Pattern.compile(
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    ).matcher(email).matches()
}

