package com.qbrains.tampcolapp.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.qbrains.tampcolapp.BuildConfig
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.GoogleAuthRequest
import com.qbrains.tampcolapp.data.network.api.GoogleAuthResponse
import com.qbrains.tampcolapp.data.network.api.IGoogleAPI
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMUser
import com.qbrains.tampcolapp.databinding.ActivityLoginBinding
import com.qbrains.tampcolapp.databinding.DialogOtpVerificationBinding
import com.qbrains.tampcolapp.databinding.DialogPhVerificationBinding
import com.qbrains.tampcolapp.ui.extension.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*, AppBarLayout.OnOffsetChangedListener*/
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var phnBinding: DialogPhVerificationBinding
    private lateinit var otpBinding: DialogOtpVerificationBinding

    val vmUser: VMUser by lazy {
        this.let {
            ViewModelProvider(it, VMUser.Factory(this))
                .get(VMUser::class.java)
        }
    }
    private lateinit var otpHash: String
    private var googleIntentLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    val callbackManager = CallbackManager.Factory.create()
    val firebaseAuth = FirebaseAuth.getInstance()
    val loginManager = LoginManager.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this@LoginActivity)

//        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        /**   collapsingToolbar.visibility = View.VISIBLE
        collapsingToolbar.title = " "
        appBarLayout.addOnOffsetChangedListener(this)
         */
        binding.skipBtn.setOnClickListener {
            finish()
            startActivity(Intent(this, DashBoardActivity::class.java))
        }
        binding.loginButton.setOnClickListener {
            validateAllFields()
        }
        binding.tvForgot.setOnClickListener {
            startActivity(Intent(this, ForgetActivity::class.java))
        }
        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.loginWithotp.setOnClickListener {
            Log.e("CALLED", "CLICK")
            showDialog()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(getString(R.string.google_client_id))
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        googleIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

                Log.e("ERROR", result.toString())
                Log.e("ERROR", result.data.toString())
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let {
                        try {
                            val task = GoogleSignIn.getSignedInAccountFromIntent(it)
                            val account: GoogleSignInAccount =
                                task.getResult(ApiException::class.java)
                            // builder and passing our base url

                            val retrofit = Retrofit.Builder()
                                .baseUrl(BuildConfig.GOOGLE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                            val retrofitAPI = retrofit.create(IGoogleAPI::class.java)

                            retrofitAPI.googleLogin(
                                GoogleAuthRequest(
                                    "authorization_code",
                                    getString(R.string.google_client_id),
                                    getString(R.string.google_client_secret),
                                    "",
                                    account.serverAuthCode ?: ""
                                )
                            )
                                .enqueue(object : Callback<GoogleAuthResponse> {
                                    override fun onResponse(
                                        call: Call<GoogleAuthResponse>,
                                        response: Response<GoogleAuthResponse>
                                    ) {
                                        vmUser.googleLogin(response.body()?.idToken ?: "",
                                            {
                                                binding.pbUserLogin.visibility = View.GONE
                                                Log.e("taggg", it.success + it.email + it.message)
                                                if (it.success == "1") {

                                                    PreferenceManager(this@LoginActivity).setIsLoggedIn(
                                                        true
                                                    )
                                                    PreferenceManager(this@LoginActivity).setCustomerId(
                                                        it.user_id.toString()
                                                    )
                                                    finish()
                                                    startActivity(
                                                        Intent(
                                                            this@LoginActivity,
                                                            DashBoardActivity::class.java
                                                        )
                                                    )
                                                } else if (it.success == "0") {
                                                    it.message?.let { it1 -> toast(it1) }
                                                }
                                            },
                                            {})
                                        Log.i("onResponse: ", Gson().toJson(response.body()))
                                    }

                                    override fun onFailure(
                                        call: Call<GoogleAuthResponse>,
                                        t: Throwable
                                    ) {
                                        Log.i("onResponse: ", Gson().toJson(t.localizedMessage))
                                    }
                                })
//                            vmUser.googleJWTToken(account.serverAuthCode ?: "", {}, {})
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        binding.ivGoogleLogin.setOnClickListener {
            GoogleSignIn.getLastSignedInAccount(this)
            val intent = mGoogleSignInClient.signInIntent
            googleIntentLauncher?.launch(intent)
        }

        binding.ivFacebookLogin.setOnClickListener {
            binding.pbUserLogin.visibility = View.VISIBLE
            loginManager.logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )

        }

        runOnUiThread {
            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    vmUser.facebookLogin(result.accessToken.token, {
                        it.email?.let { it1 -> Log.e("response", it1) }
                        binding.pbUserLogin.visibility = View.GONE
                        if (it.success == "1") {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            PreferenceManager(this@LoginActivity).setIsLoggedIn(
                                true
                            )
                            PreferenceManager(this@LoginActivity).setCustomerId(
                                it.user_id.toString()
                            )

                            PreferenceManager(this@LoginActivity).setUserPhoneNumber(
                                it.phone.toString()
                            )
                            PreferenceManager(this@LoginActivity).setUserEmailId(
                                it.email.toString()
                            )
                            finish()
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    DashBoardActivity::class.java
                                )
                            )
                        } else {
                            binding.pbUserLogin.visibility = View.GONE
                            toast(it.message.toString())
                        }
                    }, {})
                    /* val credential =
                     FacebookAuthProvider.getCredential(result.accessToken.token)
                 firebaseAuth.signInWithCredential(credential)
                     .addOnCompleteListener(this@LoginActivity) { task ->
                         if (task.isSuccessful) {
                             // Sign in success, update UI with the signed-in user's information
                             val user = firebaseAuth.currentUser
                             Log.i( "onSuccess: ","${user?.email} -- ${user?.displayName}")
                         } else {
                             // If sign in fails, display a message to the user.
                             task.exception?.printStackTrace()
                         }
                     }
                     .addOnFailureListener {
                         it.printStackTrace()
                     }*/
                }

                override fun onCancel() {
                    Log.i("onCancel: ", "Cancel")
                }

                override fun onError(error: FacebookException) {
                    error.printStackTrace()
                    Log.i("onCancel: ", error.message.toString())
                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        } else {
            binding.pbUserLogin.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        /**      moveToDashBoard()  //todo important
         */
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure?")
            .setPositiveButton("yes",
                DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(Intent.ACTION_MAIN).setClassName(/* TODO: provide the application ID. For example: */
                        packageName,
                        "com.razorpay.CheckoutActivity"
                    )
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finishAffinity()
                }).setNegativeButton("no", null).show()
    }


    private fun validateAllFields() {
        try {
            when {
                TextUtils.isEmpty(binding.etEmailId!!.text) -> binding.etEmailId.error =
                    getString(R.string.label_error_email)
                TextUtils.isEmpty(binding.etPassword!!.text) -> binding.etPassword.error =
                    getString(R.string.label_error_password)

                else -> {
                    binding.pbUserLogin.visibility = View.VISIBLE
                    Log.e(
                        "tag11",
                        binding.etPassword!!.text.toString()
                    )
                    vmUser.userLogin(binding.etEmailId!!.text.toString(),
                        binding.etPassword!!.text.toString(), onSuccess = {
                            binding.pbUserLogin.visibility = View.GONE
                            Log.e("taggg", it.success + it.email + it.message)
                            if (it.success == "1") {

                                PreferenceManager(this).setIsLoggedIn(true)
                                PreferenceManager(this).setCustomerId(it.user_id.toString())
                                finish()
                                startActivity(Intent(this, DashBoardActivity::class.java))
                            } else if (it.success == "0") {
                                it.message?.let { it1 -> toast(it1) }
                            }

                        }, onError = {

                            binding.pbUserLogin.visibility = View.GONE


                        });

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun moveToDashBoard() {
        if (PreferenceManager(this).getIsLoggedIn()) {
            finish()
            startActivity(Intent(this, DashBoardActivity::class.java))
        }

    }

    /**  override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
    runOnUiThread {
    var scrollRange = -1
    if (scrollRange == -1) {
    scrollRange = appBarLayout?.totalScrollRange!!
    }

    if (scrollRange + verticalOffset == 0) {
    //                tv_Collpasing.text = ""
    tvToolbar.text = getString(R.string.app_name)

    } else {
    //                tv_Collpasing.text = getString(R.string.app_name)
    tvToolbar.text = ""
    }
    }
    }
     */

    private fun showDialog() {
        Log.e("CALLED", "FUNCTION CALLED")
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        phnBinding = DialogPhVerificationBinding.inflate(layoutInflater)

        dialog.setContentView(phnBinding.root)
        phnBinding.buttonVerifyPhone.setOnClickListener {
            verifyPhoneNumber(phnBinding.textInputEditTextPhone.text.toString(), dialog)
        }
        dialog.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        phnBinding.closeDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun verifyPhoneNumber(phoneNumber: String, dialog: Dialog) {


        if (phoneNumber.length == 10) {
            vmUser.loginViaOtp(phoneNumber,
                onSuccess = {
                    if (it.success == "1") {
                        otpHash = phoneNumber
                        showDialog2()
                        dialog.dismiss()
                    } else
                        toast(it.message.toString())
                },
                onError = {

                })

        } else
            toast("Please Enter Valid Mobile Number")
    }


    private fun showDialog2() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        otpBinding = DialogOtpVerificationBinding.inflate(layoutInflater)
//        dialog.setContentView(R.layout.dialog_otp_verification)
        dialog.setContentView(otpBinding.root)
//        Log.d("TAG", "Button guys ---> xx");
        otpBinding.buttonVerifyOtp.setOnClickListener {
//            Log.d("TAG", otpBinding.otpEditText.text.toString());
            verifyotp(otpBinding.otpEditText.text.toString())
        }
        otpBinding.closeOtpDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show()

    }

    private fun verifyotp(otp: String) {

        showLoader(true)
        vmUser.verifyOtp(otp, otpHash,
            onSuccess = {
                if (it.success == "1") {
                    showLoader(false)
                    PreferenceManager(this).setIsLoggedIn(true)
                    PreferenceManager(this).setCustomerId(it.user_id.toString())
                    PreferenceManager(this).setUserPhoneNumber(it.phone.toString())
                    PreferenceManager(this).setUserEmailId(it.email.toString())
                    finish()
                    startActivity(Intent(this, DashBoardActivity::class.java))

                } else{
                    showLoader(false)
                    toast(it.message.toString())
                }
            },
            onError = {
                showLoader(false)
            })
    }

    private fun showLoader(show: Boolean) {
//        if (!isAdded || _binding == null) return

        binding.pbUserLogin.visibility =
            if (show) View.VISIBLE else View.GONE
    }
}