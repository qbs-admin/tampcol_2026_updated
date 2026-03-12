package com.qbrains.tampcolapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.badge.BadgeDrawable
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMUser
import com.qbrains.tampcolapp.databinding.ActivityDashboardBinding
import com.qbrains.tampcolapp.ui.extension.hide
import com.qbrains.tampcolapp.ui.extension.show
import com.qbrains.tampcolapp.ui.extension.showDialAlert
import com.qbrains.tampcolapp.ui.extension.toast
import com.razorpay.PaymentResultListener


class DashBoardActivity : AppCompatActivity(), PaymentResultListener {
    lateinit var navController: NavController
    private var menuItem: MenuItem? = null

    var db: LaaFreshDAO? = null

    val transactionId: MutableLiveData<String?> = MutableLiveData()

    val callbackManager = CallbackManager.Factory.create()
    val loginManager = LoginManager.getInstance()

    val vmUser: VMUser by lazy {
        this.let {
            ViewModelProvider(it, VMUser.Factory(this))
                .get(VMUser::class.java)
        }
    }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LaaFreshDB.getInstance(context = this)?.laaFreshDAO()
        setCartCount(0)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.dashboardNavHost) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.selectedItemId = R.id.homeFragment
        binding.bottomNav.setOnNavigationItemSelectedListener {
            menuItem = it
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.favoriteFragment -> {
                    if (!PreferenceManager(this).getIsLoggedIn()) {
                        navController.navigate(R.id.loginFragment)
                    } else {
                        navController.navigate(R.id.favoriteFragment)
                    }
                }
                R.id.myOrdersFragment -> {
                    if (!PreferenceManager(this).getIsLoggedIn()) {
                        navController.navigate(R.id.loginFragment)
                    } else {
                        navController.navigate(R.id.myOrdersFragment)
                    }
                }
                R.id.settingsFragment -> {
                    if (!PreferenceManager(this).getIsLoggedIn()) {
                        navController.navigate(R.id.loginFragment)
                    } else {
                        navController.navigate(R.id.settingsFragment)
                    }
                }
                R.id.myCartFragment -> {
                    if (!PreferenceManager(this).getIsLoggedIn()) {
                        navController.navigate(R.id.loginFragment)
                    } else {
                        navController.navigate(R.id.myCartFragment)
                    }
                }

            }
            return@setOnNavigationItemSelectedListener false
        }
        visibilityNavElements(navController)

        runOnUiThread {
            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    vmUser.facebookLogin(result.accessToken.token, {
                        it.email?.let { it1 -> Log.e("response", it1) }
                        if (it.success == "1") {
                            Toast.makeText(
                                this@DashBoardActivity,
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            PreferenceManager(this@DashBoardActivity).setIsLoggedIn(
                                true
                            )
                            PreferenceManager(this@DashBoardActivity).setCustomerId(
                                it.user_id.toString()
                            )

                            PreferenceManager(this@DashBoardActivity).setUserPhoneNumber(
                                it.phone.toString()
                            )
                            PreferenceManager(this@DashBoardActivity).setUserEmailId(
                                it.email.toString()
                            )
                            navController.navigateUp()

                        } else {
                            toast(it.message.toString())
                        }
                    }, {})
                }

                override fun onCancel() {
                    Log.i("onCancel: ", "O")
                }

                override fun onError(error: FacebookException) {
                    error.printStackTrace()
                    Log.i("onError: ", error.message.toString())
                }
            })
        }

    }

    fun visibilityNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> binding.bottomNav.show()
                R.id.favoriteFragment -> binding.bottomNav.show()
                R.id.myOrdersFragment -> binding.bottomNav.show()
                R.id.settingsFragment -> binding.bottomNav.show()
                R.id.myCartFragment -> binding.bottomNav.show()
                else -> binding.bottomNav.hide()
            }
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.homeFragment -> {
                this.showDialAlert(title = getString(R.string.label_exit), result = {
                    if (it) {
                        finishAffinity()
                    }
                })
            }
            else -> {
                navController.navigateUp()
            }
        }
    }

    fun setCartCount(badgeCount: Int) {
        val badge: BadgeDrawable = binding.bottomNav.getOrCreateBadge(R.id.myCartFragment)
        if (badgeCount == 0) {
            badge.isVisible = false
        } else {
            badge.isVisible = true
            badge.number = badgeCount
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        transactionId.postValue(p0)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        // Handle error
        Toast.makeText(this, "PAYMENT ERROR, TRY AGAIN LATER",Toast.LENGTH_SHORT).show();
        Log.e("PAY ERROR", p0.toString());
        Log.e("PAY ERROR 1", p1.toString());

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
