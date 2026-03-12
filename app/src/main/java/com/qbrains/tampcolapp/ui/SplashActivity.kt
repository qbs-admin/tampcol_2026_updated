package com.qbrains.tampcolapp.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.qbrains.tampcolapp.BuildConfig
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.network.api.request.SendTokenRequest
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMAccount
import com.qbrains.tampcolapp.databinding.ActivitySplashBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashActivity : AppCompatActivity() {

    companion object {
        const val MY_IN_APP_UPDATE_REQUEST_CODE = 1000
    }

    var db: LaaFreshDAO? = null

    var userId = ""

    private lateinit var binding: ActivitySplashBinding

    val vmAccount: VMAccount by lazy {
        this.let {
            ViewModelProvider(it, VMAccount.Factory(this))
                .get(VMAccount::class.java)
        }
    }

//    private lateinit var appUpdateManager: AppUpdateManager
//    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
//      initFirebase()
//        loadSplash()
//        if (vmAccount.isNewUpdateAvailable.get() == false) {
            loadSplash()
//        }
        initFirebase()

        try {
            val info = packageManager.getPackageInfo(
               BuildConfig.APPLICATION_ID,
                PackageManager.GET_SIGNATURES
            )
            val signatures = info.signatures
            signatures?.let{
                for (signature in signatures) {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val sign: String = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                    Log.e("MY KEY HASH:", sign)
                    //Toast.makeText(applicationContext, sign, Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

      /*  installStateUpdatedListener = InstallStateUpdatedListener { state ->
            when (state.installStatus()) {
                InstallStatus.DOWNLOADING -> {

                }
                InstallStatus.DOWNLOADED -> {
                    popupSnackBarForCompleteUpdate()
                }
                InstallStatus.CANCELED -> {

                }
                InstallStatus.FAILED -> {

                }
                InstallStatus.INSTALLED -> {

                }
                InstallStatus.INSTALLING -> {

                }
                InstallStatus.PENDING -> {

                }
                InstallStatus.REQUIRES_UI_INTENT -> {

                }
                InstallStatus.UNKNOWN -> {
                    appUpdateManager.unregisterListener(installStateUpdatedListener)
                }
            }
        }
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.registerListener(installStateUpdatedListener)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this::startIntentSenderForResult,
                    MY_IN_APP_UPDATE_REQUEST_CODE
                )
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                vmAccount.isNewUpdateAvailable.set(true)
                *//*if (appUpdateInfo.updatePriority() >= 4 ||
                    (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= 7
                ) {*//*
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this::startIntentSenderForResult,
                    MY_IN_APP_UPDATE_REQUEST_CODE
                )
                *//*} else {
                    if (isAdded)
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            this::startIntentSenderForResult,
                            MY_IN_APP_UPDATE_REQUEST_CODE
                        )
                }*//*
            } else {
                vmAccount.isNewUpdateAvailable.set(false)
            }
        }
        appUpdateManager.appUpdateInfo.addOnFailureListener {
            it.printStackTrace()
            vmAccount.isNewUpdateAvailable.set(false)
        }*/
    }

    private fun loadSplash() {
        Handler().postDelayed({
            moveToDashBoard()
        }, 3000)
    }

    private fun moveToDashBoard() {
        if (PreferenceManager(this).getIsLoggedIn() && !PreferenceManager(this).getCustomerId()
                .isNullOrBlank()
        ) {
            userId = PreferenceManager(this).getCustomerId()
            finish()
            val intent = Intent(this, DashBoardActivity::class.java)
            intent.addFlags(
                FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(intent)
        } else {
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(
                FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
            )
            startActivity(intent)
        }
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                // Get new FCM registration token
                val tokn = task.result
                sendFirebaseIdToServer(token = tokn.toString())

//                (application as HarinaApplications).setFirebaseAppId(token.toString())
            }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_IN_APP_UPDATE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                if (::appUpdateManager.isInitialized && ::installStateUpdatedListener.isInitialized) {
                    appUpdateManager.unregisterListener(installStateUpdatedListener)
                    moveToDashBoard()
                }
            }
        }
    }*/

    private fun sendFirebaseIdToServer(token: String) {
        Log.e("senddToServer: ", token)
//        val mJob = SupervisorJob()
//        val mScope = CoroutineScope(Dispatchers.IO + mJob)
        var sendTokenRequest = SendTokenRequest()
        PreferenceManager(this).setDeviceToken(token)
        sendTokenRequest.tokenId = token
        sendTokenRequest.userId = userId
        val response = vmAccount.sendToken(
            sendTokenRequest, {

            },
            {

            }
        )
    }


   /* private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(
            findViewById(android.R.id.content),
            R.string.app_update_downloaded,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.restart) {
                appUpdateManager.completeUpdate()
                moveToDashBoard()
            }
            setActionTextColor(
                ContextCompat.getColor(
                    this@SplashActivity,
                    R.color.colorBackgroundPrimary
                )
            )
            show()
        }
    }*/

}
