package com.qbrains.tampcolapp.ui.fragments.drawerFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.qbrains.tampcolapp.data.viewmodel.ContactusViewModel
import com.qbrains.tampcolapp.databinding.ContactusFragmentBinding

//import kotlinx.android.synthetic.main.contactus_fragment.*

//class ContactusFragment : Fragment() {
////    private var mViewModel: ContactusViewModel? = null
//
//    private var _binding : ContactusFragmentBinding?=null
//    private val binding = _binding!!
//
//    val vmContactus: ContactusViewModel by lazy {
//        this.let {
//            ViewModelProvider(it, ContactusViewModel.Factory(requireContext()))
//                .get(ContactusViewModel::class.java)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        _binding = ContactusFragmentBinding.inflate(layoutInflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.tbAddAddress.setOnClickListener {
//            findNavController().navigateUp()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        mViewModel = ViewModelProvider(this).get(
////            ContactusViewModel::class.java
////        )
//        showLoading(true)
///*
//        vmContactus.getContactDetails({
//            Log.e( "onActivityCreatedtag: ",it.site_address )
//        },{
//            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//        }
//        )
//*/
//
//        vmContactus.getContactDetails({
//            binding.clGetInTouch.visibility = View.VISIBLE
//            binding.siteAddressTxt.text = it.site_address
//            binding.siteEmailTxt.text = it.site_email
//            binding.sitePhTxt.text = it.site_phone
//            binding.siteTimingTxt.text = it.site_timing
//            Log.e("onActivityCreatedtag: ", it.site_address)
//            showLoading(isLoading = false)
//        }, {
//            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
//            showLoading(isLoading = false)
//        }
//        )
//    }
//
//    companion object {
//        fun newInstance(): ContactusFragment {
//            return ContactusFragment()
//        }
//    }
//
//    private fun showLoading(isLoading: Boolean) {
//        if (binding.pbContactUs != null) {
//            if (isLoading)
//                binding.pbContactUs.visibility = View.VISIBLE
//            else
//                binding.pbContactUs.visibility = View.GONE
//        }
//    }
//}


class ContactusFragment : Fragment() {
    private var _binding: ContactusFragmentBinding? = null
    private val binding get() = _binding!!

    val vmContactus: ContactusViewModel by lazy {
        ViewModelProvider(this, ContactusViewModel.Factory(requireContext()))
            .get(ContactusViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactusFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbAddAddress.setOnClickListener {
            findNavController().navigateUp()
        }

        showLoading(true)
        vmContactus.getContactDetails(
            {
                binding.clGetInTouch.visibility = View.VISIBLE
                binding.siteAddressTxt.text = it.site_address
                binding.siteEmailTxt.text = it.site_email
                binding.sitePhTxt.text = it.site_phone
                binding.siteTimingTxt.text = it.site_timing
                Log.e("onViewCreated:", it.site_address)
                showLoading(false)
            },
            {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbContactUs.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
