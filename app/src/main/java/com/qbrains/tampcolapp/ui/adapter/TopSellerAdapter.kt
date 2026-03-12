package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.reponse.blockItem
import com.qbrains.tampcolapp.databinding.ItemTopSellerBinding

typealias onClikTopSellerItem = (ItemId: String, product: blockItem, addToCart: Boolean, varientPosition: Int) -> Unit

class TopSellerAdapter(private val onClikTopSellerItem: onClikTopSellerItem) : RecyclerView.Adapter<TopSellerAdapter.ViewHolder>() {
    private var topseller = ArrayList<blockItem>()

    inner class ViewHolder(private val binding: ItemTopSellerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = topseller[position]
            val varientPosition: Int
            binding.apply {
                varientPosition = 0

                // Prepare the spinner list
                val spinnerList = item.price_variant?.map { it.product_weight.toString() } ?: emptyList()

                val adapter = ArrayAdapter(root.context, R.layout.spinner_item, spinnerList)
                tsSpinner.adapter = adapter

                tsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val priceVariant = item.price_variant?.get(position)
                        val price = priceVariant?.price?.toDouble() ?: 0.0
                        val discount = priceVariant?.discount?.toDouble() ?: 0.0
                        val discountPercent = priceVariant?.discount_percent ?: ""

                        tsPrice.text = root.context.getString(R.string.Rs) + price.toString()
                        if (discountPercent.isNotEmpty()) {
                            val discountPrice = price + discount
                            tsDiscount.text = root.context.getString(R.string.Rs) + discountPrice.toString()
                            tsOferTxt.text = discountPercent + "%"
                            sellerDiscountLayout.visibility = View.VISIBLE
                            tsOferImg.visibility = View.VISIBLE

                            Glide.with(tsImg)
                                .load(priceVariant?.image)
                                .into(tsImg)
                        } else {
                            sellerDiscountLayout.visibility = View.GONE
                            tsOferImg.visibility = View.GONE
                        }
                    }
                }

                // Set product details
                tsProductName.text = item.title
                Glide.with(tsImg)
                    .load(item.price_variant?.firstOrNull()?.image)
                    .into(tsImg)

                root.setOnClickListener {
                    onClikTopSellerItem(item.product_id.toString(), item, false, varientPosition)
                }

                addBtn.setOnClickListener {
                    onClikTopSellerItem(item.product_id.toString(), item, true, varientPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    override fun getItemCount(): Int = topseller.size

    fun addDashBoard(topsellers: List<blockItem>) {
        topseller.clear()
        topseller.addAll(topsellers)
        notifyDataSetChanged()
    }

    class ItemCategoryAdapterDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(space, space, space, space)
        }
    }
}
