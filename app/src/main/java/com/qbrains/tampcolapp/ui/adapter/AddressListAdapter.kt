package com.qbrains.tampcolapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qbrains.tampcolapp.data.network.api.reponse.AddressItem
import com.qbrains.tampcolapp.databinding.ListItemAddressBinding

typealias addressItem = (addressItem: AddressItem) -> Unit
typealias addressOnSelected = (addressItem: AddressItem) -> Unit
typealias addressSelected = (id: String) -> Unit

class AddressListAdapter(private val addressSelected: addressSelected) :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    private val resAddressList = ArrayList<AddressItem>()
    private var addressId = ""

    inner class ViewHolder(private val binding: ListItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val addressItem = resAddressList[position]

            binding.apply {
                tvCustomerAddress.text = "${addressItem.address1} ${addressItem.address2}"
                tvAddressListDeliveryPhoneNumber.text = addressItem.phone

                root.setOnClickListener {
                    addressSelected.invoke(addressItem.id.toString())
                }

                /*ivRightIcon.setOnClickListener {
                    addressItem.invoke(resAddressList[position])
                }
                root.setOnClickListener {
                    addressOnSelected.invoke(resAddressList[position])
                }
                if (resAddressList[position].addressId == addressId) {
                    tvCustomerAddress.setTextColor(ContextCompat.getColor(root.context, R.color.colorPrimary))
                    tvAddressListDeliveryPhoneNumber.setTextColor(ContextCompat.getColor(root.context, R.color.colorPrimary))
                } else {
                    tvCustomerAddress.setTextColor(ContextCompat.getColor(root.context, R.color.textHeader))
                    tvAddressListDeliveryPhoneNumber.setTextColor(ContextCompat.getColor(root.context, R.color.textHeader))
                }
                tvCustomerAddress.text =
                    "${resAddressList[position].firstname} ${resAddressList[position].lastname}\n" +
                    "${resAddressList[position].address_1}\n" +
                    "${resAddressList[position].address_2},\n" +
                    "${resAddressList[position].city} ${resAddressList[position].postcode}"
                tvAddressListDeliveryPhoneNumber.text = resAddressList[position].delivery_phone_number*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = resAddressList.size

    fun listAddress(resAddress: List<AddressItem>) {
        resAddressList.clear()
        resAddressList.addAll(resAddress)
        notifyDataSetChanged()
    }
}
