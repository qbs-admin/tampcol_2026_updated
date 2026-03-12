package com.qbrains.tampcolapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.request.AddAddressRequest
import com.qbrains.tampcolapp.data.network.api.request.GetAddressDetailsRequest
import com.qbrains.tampcolapp.data.network.api.request.PincodeRequest
import com.qbrains.tampcolapp.data.network.api.request.UpdateAddressRequest
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMAccount
import com.qbrains.tampcolapp.databinding.FragmentAddAddressBinding
import com.qbrains.tampcolapp.homemvc.retrofit.isValidMobile
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.fragment_add_address.*

class AddAddressFragment : Fragment() {

    private var _binding: FragmentAddAddressBinding?=null
    private val binding get() = _binding!!

    var addAddressRequest = AddAddressRequest()
    var updateAddressRequest = UpdateAddressRequest()
    var getAddressDetailsRequest = GetAddressDetailsRequest()
    var addressDetails = "";
    val areaArrays = ArrayList<String>()
    val vmAccount: VMAccount by lazy {
        this.let {
            ViewModelProvider(it, VMAccount.Factory(requireContext()))
                .get(VMAccount::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {
//        View = inflater.inflate(R.layout.fragment_add_address, container, false)

        _binding = FragmentAddAddressBinding.inflate(inflater, container, false);
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString("AddressDetails", "") != null) {
            addressDetails = arguments?.getString("AddressDetails", "")!!
            binding.deleteAddressButton.visibility = View.VISIBLE
            binding.addAddressButton.text = "Update Address"
            binding.deleteAddressButton.setOnClickListener {
                deleteAddress(addressDetails)
            }
            binding.tbAddAddress.title = "Edit Address"
        }
        initAppBar()
        binding.addAddressButton.setOnClickListener {
            validateAllFields()
        }

        vmAccount.getAddress(
            onSuccess = {
                if (it.serviceArea?.isNotEmpty() == true) {
//                    areaArrays.add("Area")
                    areaArrays.addAll(it.serviceArea as List<String>)
                    val arrayAdapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        areaArrays.toList()
                    )
                    arrayAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item
                    )
                    binding.areaSpinner.adapter = arrayAdapter
                    getAddressDetails(addressDetails)
                }

            },
            onError = {}
        )
    }

    fun deleteAddress(addressDetails: String) {
        binding.pbAddAddress.visibility = View.VISIBLE
        getAddressDetailsRequest.id = addressDetails
        vmAccount.deleteAddress(getAddressDetailsRequest, onSuccess = {
            if (it.success == "1") {
                binding.pbAddAddress.visibility = View.GONE
                findNavController().navigate(R.id.action_addAddressFragment_to_settingsFragment,
                    null,
                    navOptions {
                        popUpTo(R.id.addAddressFragment) {
                            inclusive = true
                        }
                    })

            } else {
                binding.pbAddAddress.visibility = View.GONE
            }
        },
            onError = {
                binding.pbAddAddress.visibility = View.GONE

            }

        )
    }

    private fun getAddressDetails(addressDetailsId: String) {
        val getAddressDetailsRequest = GetAddressDetailsRequest()
        getAddressDetailsRequest.id = addressDetailsId
        if (isConnected(requireActivity())) {
            vmAccount.getAddressDetails(getAddressDetailsRequest, onSuccess = {
                binding.etFirstName.setText(it.address?.get(0)?.name)
                binding.etLastName.setText(it.address?.get(0)?.lastName)
                binding.etAddressOne.setText(it.address?.get(0)?.address1)
//                etAddressTwo.setText(it.address?.get(0)?.address2)
                binding.etMobile.setText(it.address?.get(0)?.phone)
                binding.etCity.setText(it.address?.get(0)?.city)
                binding.etState.setText(it.address?.get(0)?.state)
                binding.etCountry.setText(it.address?.get(0)?.area)
                binding.etPinCode.setText(it.address?.get(0)?.zip)
                binding.areaSpinner.setSelection(areaArrays.indexOf(it.address?.get(0)?.area))
                binding.chkDefaultAddress.isChecked = it.address?.get(0)?.defaultAddress.equals("true")
            }, onError = {

            })
        } else {
            activity?.toast("Please Check the your Internet! ")
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
                TextUtils.isEmpty(binding.etFirstName!!.text) -> binding.etFirstName.error =
                    getString(R.string.label_add_name)
                TextUtils.isEmpty(binding.etLastName!!.text) -> binding.etLastName.error =
                    getString(R.string.label_add_last_name)
                TextUtils.isEmpty(binding.etAddressOne!!.text) -> binding.etAddressOne.error =
                    getString(R.string.label_add_address_one)
//                TextUtils.isEmpty(etAddressTwo!!.text) -> etAddressTwo.error =
//                    getString(R.string.label_add_address_two)
                TextUtils.isEmpty(binding.etMobile!!.text) || !isValidMobile(binding.etMobile.text.toString())
                -> binding.etMobile.error = getString(R.string.label_add_mobile)
                TextUtils.isEmpty(binding.etCity!!.text) -> binding.etCity.error =
                    getString(R.string.label_add_city)
                TextUtils.isEmpty(binding.etState!!.text) -> binding.etState.error =
                    getString(R.string.label_add_state)
//                TextUtils.isEmpty(etCountry!!.text) -> etCountry.error =
//                    getString(R.string.label_add_country)
                TextUtils.isEmpty(binding.etPinCode!!.text) -> binding.etPinCode.error =
                    getString(R.string.label_add_pinCode)
                else -> {
                    binding.pbAddAddress.visibility = View.VISIBLE
                    val pinCodeRequest = PincodeRequest()
//                    pinCodeRequest.zipcode = etCountry.text.toString()
                    pinCodeRequest.zipcode = areaArrays[binding.areaSpinner.selectedItemPosition]
                    if (arguments?.getString("AddressDetails", "") != null) {
                        addressDetails = arguments?.getString("AddressDetails", "")!!
                        updateAddressRequest.userId =
                            PreferenceManager(requireContext()).getCustomerId()
                        updateAddressRequest.name = binding.etFirstName.text.toString()
                        updateAddressRequest.lastName = binding.etLastName.text.toString()
                        updateAddressRequest.address1 = binding.etAddressOne.text.toString()
                        updateAddressRequest.address2 = binding.etAddressTwo.text.toString()
                        updateAddressRequest.phone = binding.etMobile.text.toString()
                        updateAddressRequest.city = binding.etCity.text.toString()
                        updateAddressRequest.state = binding.etState.text.toString()
                        updateAddressRequest.area = areaArrays[binding.areaSpinner.selectedItemPosition]
                        updateAddressRequest.zip = binding.etPinCode.text.toString()
                        updateAddressRequest.langlat = ""
                        updateAddressRequest.defaultAddress =
                            (binding.chkDefaultAddress.isChecked.toString())
                        updateAddressRequest.id = addressDetails
                    } else {
                        addAddressRequest.userId =
                            PreferenceManager(requireContext()).getCustomerId()
                        addAddressRequest.firstName = binding.etFirstName.text.toString()
                        addAddressRequest.lastName = binding.etLastName.text.toString()
                        addAddressRequest.address1 = binding.etAddressOne.text.toString()
//                        addAddressRequest.address2 = etAddressTwo.text.toString()
                        addAddressRequest.phone = binding.etMobile.text.toString()
                        addAddressRequest.city = binding.etCity.text.toString()
                        addAddressRequest.state = binding.etState.text.toString()
                        addAddressRequest.area = areaArrays[binding.areaSpinner.selectedItemPosition]
                        addAddressRequest.zip = binding.etPinCode.text.toString()
                        addAddressRequest.langlat = ""
                        addAddressRequest.defaultAddress = (binding.chkDefaultAddress.isChecked.toString())
                    }

                    vmAccount.pinCode(pinCodeRequest, onSuccess = {
                        if (it.success == "1") {
                            binding.pbAddAddress.visibility = View.GONE
                            if (arguments?.getString("AddressDetails", "") != null) {
                                addressDetails = arguments?.getString("AddressDetails", "")!!
                                updateAddress(updateAddressRequest)
                            } else
                                addAddress(addAddressRequest)
                        } else {
                            activity?.toast(it.message.toString())
                            binding.pbAddAddress.visibility = View.GONE
                        }
                    }, onError = {
                        binding.pbAddAddress.visibility = View.GONE

                    })
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateAddress(updateAddressRequest: UpdateAddressRequest) {
        binding.pbAddAddress.visibility = View.VISIBLE
        vmAccount.updateAddress(updateAddressRequest, onSuccess = {
            if (it.success == "1") {
                binding.pbAddAddress.visibility = View.GONE
                findNavController().navigate(R.id.action_addAddressFragment_to_settingsFragment,
                    null,
                    navOptions {
                        popUpTo(R.id.addAddressFragment) {
                            inclusive = true
                        }
                    })
                requireActivity().toast(it.message ?: "")
            } else {
                binding.pbAddAddress.visibility = View.GONE
            }
        },
            onError = {
                binding.pbAddAddress.visibility = View.GONE

            }

        )
    }

    fun addAddress(addAddressRequest: AddAddressRequest) {
        binding.pbAddAddress.visibility = View.VISIBLE
        vmAccount.addAddress(addAddressRequest, onSuccess = {
            it.message?.let { it1 -> Log.e("tagadres", it1) }
            if (it.success == "1") {
                binding.pbAddAddress.visibility = View.GONE
                findNavController().navigate(R.id.action_addAddressFragment_to_settingsFragment,
                    null,
                    navOptions {
                        popUpTo(R.id.addAddressFragment) {
                            inclusive = true
                        }
                    })


            } else {
                binding.pbAddAddress.visibility = View.GONE
                activity?.toast(it.message.toString())
            }
        },
            onError = {
                binding.pbAddAddress.visibility = View.GONE

            }

        )

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}