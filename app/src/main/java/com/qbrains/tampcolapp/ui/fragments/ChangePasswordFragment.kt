package com.qbrains.tampcolapp.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMAccount
import com.qbrains.tampcolapp.databinding.FragmentChangePasswordBinding

//import kotlinx.android.synthetic.main.fragment_change_password.*


class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding ?= null
    private val binding get() = _binding!!

    var changePasswordRequest = ChangePasswordRequest()
    val vmAccount: VMAccount by lazy {
        this.let {
            ViewModelProvider(it, VMAccount.Factory(requireContext()))
                .get(VMAccount::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* if (arguments?.getString("AddressDetails", "") != null) {
             addressDetails = arguments?.getString("AddressDetails", "")!!
             getAddressDetails(addressDetails)
             deleteAddress_button.visibility = View.VISIBLE
             addAddress_button.text = "Update Address"
             deleteAddress_button.setOnClickListener {
                 deleteAddress(addressDetails)
             }
         }*/
        initAppBar()
        binding.addAddressButton.setOnClickListener {
            validateAllFields()
        }
    }

    private fun initAppBar() {
        binding.tbAddAddress.apply {
            (activity as AppCompatActivity).setSupportActionBar(binding.tbAddAddress)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun validateAllFields() {
        try {
            when {
                TextUtils.isEmpty(binding.etCurrentPassword!!.text) -> binding.etCurrentPassword.error =
                    getString(R.string.error_current_password)
                TextUtils.isEmpty(binding.etNewPassword!!.text) -> binding.etNewPassword.error =
                    getString(R.string.error_new_password)
                TextUtils.isEmpty(binding.etConfirmPassword!!.text) -> binding.etConfirmPassword.error =
                    getString(R.string.error_confirm_password)
                else -> {
                    if (binding.etNewPassword.text.toString()
                            .equals(binding.etConfirmPassword.text.toString(), true).not()
                    ) {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.error_mismatch_password),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    binding.pbSetting.visibility = View.VISIBLE
                    changePasswordRequest.userId =
                        PreferenceManager(requireContext()).getCustomerId().toInt()
                    changePasswordRequest.oldPassword = binding.etCurrentPassword.text.toString()
                    changePasswordRequest.newPassword = binding.etConfirmPassword.text.toString()
                    vmAccount.changePassword(changePasswordRequest, onSuccess = {
                        if (it.success == "1") {
                            binding.pbSetting.visibility = View.GONE
                            Toast.makeText(
                                requireActivity(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.etCurrentPassword.text!!.clear()
                            binding.etCurrentPassword!!.clearFocus()
                            binding.etNewPassword.text!!.clear()
                            binding.etNewPassword!!.clearFocus()
                            binding.etConfirmPassword.text!!.clear()
                            binding.etConfirmPassword.clearFocus()
                            val imm: InputMethodManager =
                                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(binding.etConfirmPassword!!.windowToken, 0)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.pbSetting.visibility = View.GONE
                        }
                    }, onError = {
                        binding.pbSetting.visibility = View.GONE

                    })
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}