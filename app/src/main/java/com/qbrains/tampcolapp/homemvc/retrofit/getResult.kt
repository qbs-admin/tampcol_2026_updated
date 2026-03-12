//package com.jkapps.laafresh.homemvc.retrofit
//
//import android.util.Log
//import android.widget.Toast
//import com.google.gson.JsonObject
//import com.jkapps.laafresh.MyApplication
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class getResult {
//    var myListener: MyListener? = null
//
//    fun callForLogin(call: Call<JsonObject?>, callno: String) {
//        if (!Utils.internetChack()) {
//            Toast.makeText(
//                MyApplication.mContext,
//                "Please Check Your Internet Connection",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            call.enqueue(object : Callback<JsonObject> {
//                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                    Log.e("message", " : " + response.message())
//                    Log.e("body", " : " + response.body())
//                    Log.e("callno", " : $callno")
//                    myListener!!.callback(response.body(), callno)
//                }
//
//                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                    myListener!!.callback(null, callno)
//                    call.cancel()
//                    t.printStackTrace()
//                }
//            })
//        }
//
////        }
//    }
//
//    interface MyListener {
//        // you can define any parameter as per your requirement
//        fun callback(result: JsonObject?, callNo: String?)
//    }
//
//    fun setMyListener(Listener: MyListener?) {
//        myListener = Listener
//    }
//}