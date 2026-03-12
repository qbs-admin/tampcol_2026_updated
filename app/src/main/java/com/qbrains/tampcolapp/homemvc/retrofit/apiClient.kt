//package com.jkapps.laafresh.homemvc.retrofit
//
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class apiClient {
//    var retrofit: Retrofit? = null
//
//    var baseUrl = "https://www.mudamysore.gov.in"
//
//    var APPEND_URL = "/app/"
//
//
//    fun getInterface(): userService? {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client = OkHttpClient.Builder()
//            .addInterceptor(interceptor)
//            .build()
//        retrofit = Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//        return retrofit.create(userService::class.java)
//    }
//}