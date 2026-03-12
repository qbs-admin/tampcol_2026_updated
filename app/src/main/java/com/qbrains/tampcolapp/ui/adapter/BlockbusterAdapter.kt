package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.AppUtils
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.reponse.blockItem
import com.qbrains.tampcolapp.databinding.ItemBlockbusterBinding

typealias onClikBlockbusterItem = (ItemId: String, product: blockItem, addToCart: Boolean, varientPosition: Int) -> Unit

class BlockbusterAdapter(private val onClikBlockbusterItem: onClikBlockbusterItem) : RecyclerView.Adapter<BlockbusterAdapter.ViewHolder>() {
    var topseller = ArrayList<blockItem>()

    inner class ViewHolder(private val binding: ItemBlockbusterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            var varientPosition: Int = 0
            val item = topseller[position]

            binding.apply {
                bProductName.text = item.title
                item.productDealDays?.let {
                    tvDealExpire.text = "Expires: ${AppUtils.getFormattedDate(it)}"
                }

                val spinnerList = ArrayList<String>()
                item.price_variant?.forEach { spinnerItem ->
                    spinnerList.add(spinnerItem.product_weight.toString())
                }

                if (item.deal == "yes") {
                    bSpinner.visibility = View.GONE
                    bPrice.text = root.context.getString(R.string.Rs) + item.price_variant!![0].price.toString()

                    if (item.price_variant!![0].discount_percent!!.isEmpty()) {
                        oferImg.visibility = View.GONE
                    } else {
                        oferTxt.text = item.price_variant!![0].discount_percent + "%"
                    }

                    if (item.price_variant!![0].discount?.isNotEmpty() == true) {
                        val discountPrice = item.price_variant!![0].discount?.toDouble()!! +
                                item.price_variant!![0].price?.toDouble()!!
                        bDiscount.text = discountPrice.toString()
                    } else {
                        bbDiscountLayout.visibility = View.GONE
                    }
                } else {
                    val adapter = ArrayAdapter(root.context, R.layout.spinner_item, spinnerList)
                    bSpinner.adapter = adapter
                    bSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            bPrice.text = root.context.getString(R.string.Rs) + item.price_variant!![position].price.toString()
                            tvDealExpire.text = item.price_variant!![position].product_deal_days ?: ""

                            if (item.price_variant!![position].discount_percent!!.isEmpty()) {
                                oferImg.visibility = View.GONE
                            } else {
                                oferTxt.text = item.price_variant!![position].discount_percent + "%"
                            }

                            if (item.price_variant!![position].discount?.isNotEmpty() == true) {
                                val discountPrice = item.price_variant!![position].discount?.toDouble()!! +
                                        item.price_variant!![position].price?.toDouble()!!
                                bDiscount.text = discountPrice.toString()
                                varientPosition = position
                            } else {
                                bbDiscountLayout.visibility = View.GONE
                            }
                            Glide.with(blockbusterImg)
                                .load(item.price_variant!![position].image)
                                .into(blockbusterImg)
                        }
                    }
                }

                Glide.with(root.context)
                    .load(item.price_variant!![0].image)
                    .into(blockbusterImg)

                root.setOnClickListener {
                    onClikBlockbusterItem.invoke(item.product_id.toString(), item, false, varientPosition)
                }

                addBtn.setOnClickListener {
                    onClikBlockbusterItem.invoke(item.product_id.toString(), item, true, varientPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBlockbusterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = topseller.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addDashBoard(topsellers: List<blockItem>) {
        topseller.clear()
        topseller.addAll(topsellers)
        notifyDataSetChanged()
    }

    class ItemCategoryAdapterDecoration(private val sp: Int) : RecyclerView.ItemDecoration() {
        private var space = 0

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            space = sp
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}
