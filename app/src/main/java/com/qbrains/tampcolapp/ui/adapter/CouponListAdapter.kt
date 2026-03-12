package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.network.api.reponse.CouponItem
import com.qbrains.tampcolapp.databinding.ItemAvailableCouponBinding

typealias onClikCouponItem = (CouponCode: String) -> Unit

class CouponListAdapter(var clickItem: onClikCouponItem) : RecyclerView.Adapter<CouponListAdapter.ViewHolder>() {

    private var mCouponItem = ArrayList<CouponItem>()
    private var db: LaaFreshDAO? = null

    inner class ViewHolder(private val binding: ItemAvailableCouponBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = mCouponItem[position]
            binding.apply {
                couponCode.text = item.code
                if (item.till != null) {
                    validDateLable.visibility = View.VISIBLE
                    validDate.visibility = View.VISIBLE
                    validDate.text = item.till
                } else {
                    validDateLable.visibility = View.GONE
                    validDate.visibility = View.GONE
                }
                root.setOnClickListener {
                    clickItem.invoke(item.code.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAvailableCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mCouponItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addCouponItem(couponItems: List<CouponItem>) {
        mCouponItem.clear()
        mCouponItem.addAll(couponItems)
        notifyDataSetChanged()
    }

    class ItemCaterogyAdapterDecoration(var sp: Int) : RecyclerView.ItemDecoration() {
        private var space = 0

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            this.space = sp
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}
