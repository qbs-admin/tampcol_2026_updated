package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.table.MyCartEntity
import com.qbrains.tampcolapp.databinding.ItemCardDetailListBinding

class CheckOutViewAdapter() : RecyclerView.Adapter<CheckOutViewAdapter.ViewHolder>() {

    private var mProductItem = ArrayList<MyCartEntity>()
    private var db: LaaFreshDAO? = null
    private var myCartEntity: MyCartEntity? = null

    inner class ViewHolder(private val binding: ItemCardDetailListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = mProductItem[position]
            binding.apply {
                tvItemListName.text = "${item.productName} 1 x ${item.orderQty.toString()}"
                tvItemListPrice.text = item.orderValue.toString()
            }
        }

        private fun abbreviate(s: String): String {
            return if (s.length <= 10) s else s.substring(0, 13) + ".."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mProductItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addCartItem(cartItems: List<MyCartEntity>) {
        mProductItem.clear()
        mProductItem.addAll(cartItems)
        notifyDataSetChanged()
    }

    class ItemCaterogyAdapterDecoration(var sp: Int) : RecyclerView.ItemDecoration() {
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
