package com.qbrains.tampcolapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.viewmodel.VMRegister
import com.qbrains.tampcolapp.databinding.FragmentForgetBinding

//import kotlinx.android.synthetic.main.fragment_forget.*

class ForgetFragment : Fragment() {

    private var _binding:FragmentForgetBinding?=null
    private val binding get() = _binding!!

    val vmRegister: VMRegister by lazy {
        this.let {
            ViewModelProvider(it, VMRegister.Factory(requireContext()))
                .get(VMRegister::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentForgetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppBar()
        binding.sendFbutton.setOnClickListener {
            forgotPasswordFieldValidation()
        }
    }

    private fun initAppBar() {
        binding.tbFfrogetpassword.apply {
            (activity as AppCompatActivity).setSupportActionBar(binding.tbFfrogetpassword)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun forgotPasswordFieldValidation() {
        try {
            when {
                TextUtils.isEmpty(binding.etFForgotPassword!!.text) -> binding.etFForgotPassword.error =
                    getString(R.string.label_error_email)
                else -> {
                    binding.pbFForgotPassword.visibility = View.VISIBLE
                    vmRegister.forgotPassword(binding.etFForgotPassword!!.text.toString(),
                        onSuccess = {
                            binding.pbFForgotPassword.visibility = View.GONE
                            if (it.success == "1") {
                                findNavController().navigate(R.id.action_forgetFragment_to_loginFragment,null,
                                navOptions { popUpTo(R.id.loginFragment){
                                    inclusive=true
                                } })
                            }

                        }
                        , onError = {
                            binding.pbFForgotPassword.visibility = View.GONE

                        });

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}