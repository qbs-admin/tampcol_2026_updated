package com.qbrains.tampcolapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.network.api.reponse.MyOrderListItem
import com.qbrains.tampcolapp.data.viewmodel.VMCheckOut
import com.qbrains.tampcolapp.databinding.FragmentMyOrdersBinding
import com.qbrains.tampcolapp.ui.adapter.OderHistoryAdapter
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.fragment_my_orders.*
import java.util.*

class MyOrdersFragment : Fragment() {

    private var _binding:FragmentMyOrdersBinding?=null
    private val binding get()= _binding!!

    val vmCheckOut: VMCheckOut by lazy {
        this.let {
            ViewModelProvider(it, VMCheckOut.Factory(requireContext()))
                .get(VMCheckOut::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        if (isConnected(requireActivity())) {
            getOrderHistory()
        }
        else{
            showLoading(false)
            activity?.toast("Please Check the Internet")
        }

    }
    fun getOrderHistory(){
        vmCheckOut.getMyOrderList(onSuccess = { myOrderListResponse ->
            showLoading(false)
            if (myOrderListResponse.orders!!.size > 0) {
                binding.tvEmptyMenuOrder.visibility = View.GONE
                binding.rcHistoryList.visibility = View.VISIBLE
                setOderHistoryAdapter(myOrderListResponse.orders)
            } else {
                binding.rcHistoryList.visibility = View.GONE
                binding.tvEmptyMenuOrder.visibility = View.VISIBLE
            }

        }, onError = {
            showLoading(false)
            activity?.toast(it.toString())

        })
    }

    private fun setOderHistoryAdapter(orders: ArrayList<MyOrderListItem>) {
        binding.rcHistoryList.apply {
            binding.rcHistoryList.adapter = OderHistoryAdapter(requireContext())
        }
        (binding.rcHistoryList.adapter as OderHistoryAdapter).addHistoryItem(orders)
    }

    private fun showLoading(isLoading: Boolean) {
        if (binding.pbMyOrder != null) {
            if (isLoading)
                binding.pbMyOrder.visibility = View.VISIBLE
            else
                binding.pbMyOrder.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}


