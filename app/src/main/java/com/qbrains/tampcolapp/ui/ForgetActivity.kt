package com.qbrains.tampcolapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.viewmodel.VMRegister
import com.qbrains.tampcolapp.databinding.ActivityForgetBinding

class ForgetActivity : AppCompatActivity() {

    // View binding instance
    private lateinit var binding: ActivityForgetBinding

    val vmRegister: VMRegister by lazy {
        ViewModelProvider(this, VMRegister.Factory(this)).get(VMRegister::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set onClickListener using binding
        binding.sendButton.setOnClickListener {
            forgotPasswordFieldValidation()
        }
    }

    private fun forgotPasswordFieldValidation() {
        try {
            with(binding) {
                when {
                    TextUtils.isEmpty(etForgotPassword.text) -> etForgotPassword.error = getString(R.string.label_error_email)
                    else -> {
                        pbForgotPassword.visibility = View.VISIBLE
                        vmRegister.forgotPassword(
                            etForgotPassword.text.toString(),
                            onSuccess = {
                                pbForgotPassword.visibility = View.GONE
                                if (it.success == "1") {
                                    finish()
                                    startActivity(Intent(this@ForgetActivity, LoginActivity::class.java))
                                }
                            },
                            onError = {
                                pbForgotPassword.visibility = View.GONE
                            }
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
