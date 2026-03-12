package com.qbrains.tampcolapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.viewmodel.VMRegister
import com.qbrains.tampcolapp.homemvc.retrofit.isValidMail
import com.qbrains.tampcolapp.homemvc.retrofit.isValidMobile
import com.qbrains.tampcolapp.ui.dialog.TermsAndConditionsDialog
import com.qbrains.tampcolapp.ui.extension.toast
import com.qbrains.tampcolapp.databinding.FragmentSignupBinding // Import View Binding class

class SignUpFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSignupBinding // Declare View Binding variable
    private val vmRegister: VMRegister by lazy {
        ViewModelProvider(this, VMRegister.Factory(requireContext())).get(VMRegister::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false) // Initialize View Binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()
        binding.llFTerms.setOnClickListener(this)
        binding.signupFbutton.setOnClickListener(this)
        binding.cbFTermsAndCondition.setOnCheckedChangeListener { _, b ->
            if (b) showTermsConditions()
        }
    }

    private fun initAppBar() {
        binding.tbFfrogetpassword?.apply {
            (activity as AppCompatActivity).setSupportActionBar(this)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun validateAllFields() {
        try {
            when {
                TextUtils.isEmpty(binding.etFFirstName.text) -> binding.etFFirstName.error =
                    getString(R.string.error_first_name)
                TextUtils.isEmpty(binding.etFLastName.text) -> binding.etFLastName.error =
                    getString(R.string.error_last_name)
                TextUtils.isEmpty(binding.etFEmail.text) || !isValidMail(binding.etFEmail.text.toString()) -> binding.etFEmail.error =
                    getString(R.string.error_email)
                TextUtils.isEmpty(binding.etFPassword.text) -> binding.etFPassword.error =
                    getString(R.string.error_password)
                TextUtils.isEmpty(binding.etFMobile.text) || !isValidMobile(binding.etFMobile.text.toString()) -> binding.etFMobile.error =
                    getString(R.string.error_mobile)
                else -> {
                    binding.pbFRSignUP.visibility = View.VISIBLE
                    vmRegister.signUp(
                        binding.etFFirstName.text.toString(),
                        binding.etFLastName.text.toString(),
                        binding.etFEmail.text.toString(),
                        binding.etFPassword.text.toString(),
                        binding.etFMobile.text.toString(),
                        onSuccess = {
                            binding.pbFRSignUP.visibility = View.GONE
                            if (it.success == "1") {
                                Toast.makeText(
                                    context,
                                    "Registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(
                                    R.id.action_signUpFragment_to_loginFragment,
                                    null,
                                    navOptions {
                                        popUpTo(R.id.loginFragment) {
                                            inclusive = true
                                        }
                                    }
                                )
                            } else if (it.success == "0") {
                                Toast.makeText(
                                    context,
                                    "Your email and phone should be unique.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        onError = {
                            binding.pbFRSignUP.visibility = View.GONE
                        }
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showTermsConditions() {
        TermsAndConditionsDialog {
            binding.cbFTermsAndCondition.isChecked = it
        }.show(childFragmentManager, "")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llFTerms -> {
                showTermsConditions()
            }
            R.id.signup_Fbutton -> {
                if (binding.cbFTermsAndCondition.isChecked)
                    validateAllFields()
                else
                    activity?.toast(getString(R.string.error_terms))
            }
        }
    }
}
