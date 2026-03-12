package com.qbrains.tampcolapp.ui.component

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.FirebaseMessaging

class HarinaApplications : MultiDexApplication() {
    private lateinit var firebaseAppId: String

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
//        Firebase.messaging.isAutoInitEnabled = true
//        setAnalyticsCollectionEnabled(true);
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FacebookSdk.sdkInitialize(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun setFirebaseAppId(firebaseAppId: String) {
        this.firebaseAppId = firebaseAppId
        Log.e( "setFirebaseAppId: ",firebaseAppId )
//        sendFirebaseIdToServer()
    }

//    fun sendFirebaseIdToServer() {
//         val mJob = SupervisorJob()
//         val mScope = CoroutineScope(Dispatchers.IO + mJob)
//        val iAccountApi=IAccountRepository
//
//        mScope.launch {
//            try {
//                val response=iAccountApi.().await()
//                if (response.isSuccessful){
//                    response.body()?.let {
//                        withContext(Dispatchers.Main) { onResponse(it) }
//                    }
//                }
//            }catch (e: Exception) {
//                e.printStackTrace()
//                withContext(Dispatchers.Main) {
//                }
//
//            }
//        }
//        }


//        CoroutineScope(context = this).launch {
//
//        }
        
//          try {
//          SessionManager sessionManager;
//          User user;
//          sessionManager = new SessionManager(MyApplication.this);
//          user = sessionManager.getUserDetails("");
//          if (user != null && !user.getId().equals("0")) {
//          JSONObject jsonObject = new JSONObject();
//          JsonParser jsonParser = new JsonParser();
//          jsonObject.put("uid", user.getId());
//          jsonObject.put("fid", firebaseAppId);
//          Call<JsonObject> call = APIClient.getInterface().sendFirebaseId((JsonObject) jsonParser.parse(jsonObject.toString()));
//          GetResult getResult = new GetResult();
//          getResult.setMyListener(this);
//          getResult.callForLogin(call, "1");
//          }
//          } catch (Exception e) {
//          e.printStackTrace();
//          }
//        </JsonObject>
//    }

}