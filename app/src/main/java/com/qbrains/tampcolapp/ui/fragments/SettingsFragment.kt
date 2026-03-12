package com.qbrains.tampcolapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.reponse.AddressItem
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMAccount
import com.qbrains.tampcolapp.databinding.ContentAccountAddressBinding
import com.qbrains.tampcolapp.databinding.ContentAccountProfileBinding
import com.qbrains.tampcolapp.databinding.FragmentSettingsBinding
import com.qbrains.tampcolapp.ui.LoginActivity
import com.qbrains.tampcolapp.ui.adapter.AddressListAdapter
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.content_account_address.*
//import kotlinx.android.synthetic.main.content_account_profile.*
//import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding?=null
    private val binding get() = _binding!!

    private lateinit var addressBind: ContentAccountAddressBinding
    private lateinit var profileBind: ContentAccountProfileBinding

//    UI VARIABLES

    private lateinit var pbSetting : View
    private lateinit var rvAddress : RecyclerView

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
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        addressBind = ContentAccountAddressBinding.bind(binding.contentAddress.root)
        profileBind = ContentAccountProfileBinding.bind(binding.contentProfile.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        pbSetting = binding.pbSetting
        rvAddress = addressBind.rvAddress


        if (!PreferenceManager(requireContext()).getIsLoggedIn()) {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }



        pbSetting.visibility = View.VISIBLE
        pbSetting.visibility = View.VISIBLE

        profileData()
//        vmAccount.getProfile(onSuccess = {
//            accountView.visibility = View.VISIBLE
//            pbSetting.visibility = View.GONE
//            tvCustomerName.text = "Name: ${it.username}"
//            tvCustomerEmail.text = "Email Id: ${it.email}"
//            tvCustomerrewardpts.text = "Reward points: ${it.reward_points}"
//
//        }, onError = {
//            pbSetting.visibility = View.GONE
//        })
//        vmAccount.getAddress(
//            onSuccess = {
//
//                        setAddressAdapter(it.address as List<AddressItem>)
//
//            },
//            onError = {
//
//            }
//        )


        binding.tvChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_changePasswordFragment)
        }

        binding.tvAddNewAddress.setOnClickListener {
            findNavController().navigate(R.id.action_addAddressFragment_to_settingsFragment)
        }

        binding.tvReturnPolicy.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", "https://tampcol.com/public/api/android/v1/staticpages/refund-policy")
            bundle.putString("title", "Return Policy")
            findNavController().navigate(R.id.action_addAddressFragment_to_webViewFragment, bundle)
        }

        binding.tvContactUs.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", "https://tampcol.com/public/api/android/v1/faq")
            bundle.putString("title", "FAQ")
            findNavController().navigate(R.id.action_addAddressFragment_to_webViewFragment, bundle)
        }

        binding.tvAboutUs.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", "https://tampcol.com/public/api/android/v1/staticpages/about-us")
            bundle.putString("title", "About Us")
            findNavController().navigate(R.id.action_addAddressFragment_to_webViewFragment, bundle)
        }

        binding.tvPrivacy.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", "https://tampcol.com/public/api/android/v1/staticpages/privacy-policy")
            bundle.putString("title", "Privacy")
            findNavController().navigate(R.id.action_addAddressFragment_to_webViewFragment, bundle)
        }

        binding.tvLogout.setOnClickListener {
            PreferenceManager(requireContext()).setIsLoggedIn(false)
            PreferenceManager(requireContext()).setCustomerId("")
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)

        }
    }

   fun  profileData(){
       vmAccount.getProfile(onSuccess = {
           binding.accountView.visibility = View.VISIBLE
           pbSetting.visibility = View.GONE
           profileBind.tvCustomerName.text = "Name: ${it.username}"
           profileBind.tvCustomerEmail.text = "Email Id: ${it.email}"
           profileBind.tvCustomerrewardpts.text = "Reward points: ${it.reward_points}"

       }, onError = {
           pbSetting.visibility = View.GONE
       })
       vmAccount.getAddress(
           onSuccess = {

               setAddressAdapter(it.address as List<AddressItem>)

           },
           onError = {

           }
       )
   }



    private fun setAddressAdapter(addressItem: List<AddressItem>) {
        rvAddress.apply {
            rvAddress.adapter = AddressListAdapter {
                if (isConnected(requireActivity())) {
                    val bundle = Bundle()
                    bundle.putString("AddressDetails", it)
                    findNavController().navigate(
                        R.id.action_addAddressFragment_to_settingsFragment,
                        bundle
                    )
                } else {
                    activity?.toast("Please Check the your Internet! ")
                }

            }
        }
        (rvAddress.adapter as AddressListAdapter).listAddress(addressItem)

    }

    override fun onResume() {
//        profileData()
        super.onResume()
    }

}