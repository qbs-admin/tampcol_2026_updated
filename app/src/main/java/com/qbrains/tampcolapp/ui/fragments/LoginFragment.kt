package com.qbrains.tampcolapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
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
import com.qbrains.tampcolapp.databinding.FragmentLoginBinding
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding?=null
    private val binding get() = _binding!!
    val vmUser: VMUser by lazy {
        this.let {
            ViewModelProvider(it, VMUser.Factory(requireContext()))
                .get(VMUser::class.java)
        }
    }

    private var googleIntentLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val callbackManager = CallbackManager.Factory.create()
    val loginManager = LoginManager.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()
        binding.loginButton.setOnClickListener {
            validateAllFields()
        }
        binding.tvForgot.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_forgetFragment
            )
        }
        binding.signupBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_signUpFragment
            )
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(getString(R.string.google_client_id))
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        googleIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
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
                                                it.email?.let { it1 -> Log.e("response", it1) }
                                                binding.pbfUserLogin.visibility = View.GONE
                                                if (it.success == "1") {
                                                    Toast.makeText(
                                                        context,
                                                        "Login Successfully",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                    PreferenceManager(requireContext()).setIsLoggedIn(
                                                        true
                                                    )
                                                    PreferenceManager(requireContext()).setCustomerId(
                                                        it.user_id.toString()
                                                    )

                                                    PreferenceManager(requireContext()).setUserPhoneNumber(
                                                        it.phone.toString()
                                                    )
                                                    PreferenceManager(requireContext()).setUserEmailId(
                                                        it.email.toString()
                                                    )
                                                    findNavController().navigate(R.id.homeFragment,
                                                        null,
                                                        navOptions {
                                                            popUpTo(R.id.homeFragment) {
                                                                inclusive = true
                                                            }
                                                        })

                                                } else {
                                                    binding.pbfUserLogin.visibility = View.GONE
                                                    activity?.toast(it.message.toString())
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
            GoogleSignIn.getLastSignedInAccount(requireContext())
            val intent = mGoogleSignInClient.signInIntent
            googleIntentLauncher?.launch(intent)
        }

        binding.ivFacebookLogin.setOnClickListener {
            loginManager.logInWithReadPermissions(
                requireActivity(),
                listOf("email", "public_profile")
            )
        }


    }

    private fun validateAllFields() {
        try {
            when {
                TextUtils.isEmpty(binding.etfEmailId!!.text) -> binding.etfEmailId.error =
                    getString(R.string.label_error_email)
                TextUtils.isEmpty(binding.etfPassword!!.text) -> binding.etfPassword.error =
                    getString(R.string.label_error_password)
                else -> {
                    binding.pbfUserLogin.visibility = View.VISIBLE
                    vmUser.userLogin(binding.etfEmailId!!.text.toString(),
                        binding.etfPassword!!.text.toString(), onSuccess = {
                            it.email?.let { it1 -> Log.e("response", it1) }
                            binding.pbfUserLogin.visibility = View.GONE
                            if (it.success == "1") {
                                Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT)
                                    .show()
                                PreferenceManager(requireContext()).setIsLoggedIn(true)
                                PreferenceManager(requireContext()).setUserPhoneNumber(it.phone.toString())
                                PreferenceManager(requireContext()).setUserEmailId(it.email.toString())
                                PreferenceManager(requireContext()).setCustomerId(it.user_id.toString())
                                findNavController().navigate(R.id.homeFragment, null,
                                    navOptions {
                                        popUpTo(R.id.homeFragment) {
                                            inclusive = true
                                        }
                                    })

                            } else {
                                binding.pbfUserLogin.visibility = View.GONE
                                activity?.toast(it.message.toString())
                            }

                        }, onError = {
                            Log.e("response", it.toString())
                            binding.pbfUserLogin.visibility = View.GONE


                        });

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initAppBar() {
        binding.toolFLogin.apply {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolFLogin)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroy() {
        super.onDestroy()
        mGoogleSignInClient.signOut()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}