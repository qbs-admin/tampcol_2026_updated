package com.qbrains.tampcolapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMRegister
import com.qbrains.tampcolapp.databinding.ActivityRegisterBinding
import com.qbrains.tampcolapp.homemvc.retrofit.isValidMail
import com.qbrains.tampcolapp.homemvc.retrofit.isValidMobile
import com.qbrains.tampcolapp.ui.dialog.TermsAndConditionsDialog
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    val vmRegister: VMRegister by lazy {
        this.let {
            ViewModelProvider(it, VMRegister.Factory(this))
                .get(VMRegister::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.llTerms.setOnClickListener(this)
        binding.signupButton.setOnClickListener(this)
        binding.cbTermsAndCondition.setOnCheckedChangeListener { _, b ->
            if (b)
                showTermsAndConditions()
        }
    }


    private fun validateAllFields() {
        when {
            TextUtils.isEmpty(binding.etFirstName!!.text) -> binding.etFirstName.error =
                getString(R.string.error_first_name)
            TextUtils.isEmpty(binding.etLastName!!.text) -> binding.etLastName.error =
                getString(R.string.error_last_name)
            TextUtils.isEmpty(binding.etEmail!!.text) || !isValidMail(binding.etEmail!!.text.toString()) -> binding.etEmail.error =
                getString(R.string.error_email)
            TextUtils.isEmpty(binding.etPassword!!.text) -> binding.etPassword.error =
                getString(R.string.error_password)
            TextUtils.isEmpty(binding.etMobile!!.text) || !isValidMobile(binding.etMobile.text.toString()) -> binding.etMobile.error =
                getString(R.string.error_mobile)
            else -> {
                binding.pbRegisterLogin.visibility = View.VISIBLE
                vmRegister.signUp(binding.etFirstName!!.text.toString(),
                    binding.etLastName!!.text.toString(),
                    binding.etEmail!!.text.toString(),
                    binding.etPassword!!.text.toString(),
                    binding.etMobile!!.text.toString(),
                    onSuccess = {
                        binding.pbRegisterLogin.visibility = View.GONE
                        Log.e("TAG", " it.success")
                        if (it.success == "1") {
                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT)
                                .show()
                            PreferenceManager(this).setIsLoggedIn(false)
                            PreferenceManager(this).setUserPhoneNumber(it.phone.toString())
                            PreferenceManager(this).setUserEmailId(it.email.toString())
                            finish()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else if (it.success == "0") {
                            it.message?.let { it1 -> toast(it1) }
                        }

                    },
                    onError = {

                        binding.pbRegisterLogin.visibility = View.GONE


                    })
            }
        }
    }

    private fun showTermsAndConditions() {
        TermsAndConditionsDialog {
            binding.cbTermsAndCondition.isChecked = it
        }.show(supportFragmentManager, "")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llTerms -> {
                showTermsAndConditions()
            }

            R.id.signup_button -> {
                if (binding.cbTermsAndCondition.isChecked)
                    validateAllFields()
                else
                    toast(getString(R.string.error_terms))
            }
        }
    }
}