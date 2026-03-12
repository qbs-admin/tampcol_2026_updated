package com.qbrains.tampcolapp.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.network.api.reponse.MyOrderListItem
import com.qbrains.tampcolapp.databinding.AdapterMyOrderListBinding

class OderHistoryAdapter(private val context: Context) :
    RecyclerView.Adapter<OderHistoryAdapter.ViewHolder>() {

    private var myOrderListItem = ArrayList<MyOrderListItem>()
    var db: LaaFreshDAO? = null

    inner class ViewHolder(private val binding: AdapterMyOrderListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = myOrderListItem[position]
            binding.apply {
                orderNo.text = item.sale_id
                orderDate.text = item.sale_datetime
                paymentStatus.text = item.payment_status
                deliveryStatus.text = item.delivery_status
                orderAmount.text = context.getString(R.string.Rs) + " " + item.grand_total
                trackingId.text = item.courier_number

                // Handle tracking ID click
                val trackingIdLink = item.courier_number
                if (trackingIdLink != "pending") {
                    trackingId.setOnClickListener {
                        copyToClipboard(trackingIdLink)
                    }
                }
            }
        }

        private fun copyToClipboard(trackingId: String?) {
            val trackingUrl = "https://www.indiapost.gov.in/_layouts/15/dop.portal.tracking/trackconsignment.aspx"

            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Tracking ID", trackingId)
            clipboard.setPrimaryClip(clip)

            // Display a toast message to notify the user
            Toast.makeText(context, "Tracking ID copied to clipboard", Toast.LENGTH_SHORT).show()

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trackingUrl))
            context.startActivity(browserIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterMyOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = myOrderListItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addHistoryItem(orderListItem: ArrayList<MyOrderListItem>) {
        myOrderListItem.clear()
        myOrderListItem.addAll(orderListItem)
        notifyDataSetChanged()
    }

    class ItemCategoryAdapterDecoration(private val sp: Int) : RecyclerView.ItemDecoration() {
        private var space = 0

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            this.space = sp
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}
