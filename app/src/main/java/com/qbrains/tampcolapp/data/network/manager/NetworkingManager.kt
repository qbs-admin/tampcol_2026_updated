package com.qbrains.tampcolapp.data.network.manager

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.qbrains.tampcolapp.BuildConfig
import com.qbrains.tampcolapp.data.utility.Provider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


// Created on 12/8/19.


class NetworkingManager(private val context: Context) :
    INetworkingManager {
//    override val okHttpClient: OkHttpClient by lazy {
////
////
////        OkHttpClient.Builder().apply {
////            addNetworkInterceptor(StethoInterceptor())
////            addInterceptor(loggingInterceptor)
////        }.callTimeout(26, TimeUnit.SECONDS)
////            .readTimeout(26, TimeUnit.SECONDS)
////            .build()
////
////    }

    override val okHttpClient: OkHttpClient by lazy {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)

        val trustManagers = trustManagerFactory.trustManagers
        val trustManager = trustManagers[0] as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)

        OkHttpClient.Builder().apply {
            sslSocketFactory(sslContext.socketFactory, trustManager)
            addNetworkInterceptor(StethoInterceptor())
            addInterceptor(loggingInterceptor)
        }.callTimeout(26, TimeUnit.SECONDS)
            .readTimeout(26, TimeUnit.SECONDS)
            .build()
    }

    // okhttp logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor.Level.BODY
            false -> HttpLoggingInterceptor.Level.NONE
        }
    }


    companion object : Provider<NetworkingManager>() {
        override fun create(args: Array<out Any>): NetworkingManager {
            if (args.isEmpty()) throw IllegalArgumentException("NetworkingManager need a context")
            if (args[0] !is Context) throw IllegalArgumentException("args[0] must be a Context")
            return NetworkingManager(args[0] as Context)
        }

        inline fun <reified T> createApi(context: Context): T {
            return if (T::class.java.isInterface) {
                Retrofit.Builder()
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()
                        )
                    )
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(NetworkingManager(context).okHttpClient)
                    .build().create(T::class.java)
            } else throw IllegalArgumentException("${T::class.java.simpleName} is not an interface")
        }

        inline fun <reified T> createGoogleApi(context: Context): T {
            return if (T::class.java.isInterface) {
                Retrofit.Builder()
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()
                        )
                    )
                    .baseUrl(BuildConfig.GOOGLE_URL)
                    .client(NetworkingManager(context).okHttpClient)
                    .build().create(T::class.java)
            } else throw IllegalArgumentException("${T::class.java.simpleName} is not an interface")
        }
    }
}

interface INetworkingManager {

    val okHttpClient: OkHttpClient

}