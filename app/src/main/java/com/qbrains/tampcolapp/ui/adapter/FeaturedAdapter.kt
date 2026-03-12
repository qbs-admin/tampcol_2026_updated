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

typealias onClickFeaturedItem = (ItemId: String, product: blockItem, addToCart: Boolean, variantPosition: Int) -> Unit

class FeaturedAdapter(private val onClickItem: onClickFeaturedItem) :
    RecyclerView.Adapter<FeaturedAdapter.ViewHolder>() {

    private var topseller = ArrayList<blockItem>()

    inner class ViewHolder(private val binding: ItemTopSellerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = topseller[position]
            var variantPosition = 0

            binding.apply {
                tsProductName.text = item.title

                val spinnerList = item.price_variant?.map { it.product_weight.toString() } ?: emptyList()
                val adapter = ArrayAdapter(root.context, R.layout.spinner_item, spinnerList)
                tsSpinner.adapter = adapter

                tsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                        val selectedVariant = item.price_variant!![pos]
                        tsPrice.text = root.context.getString(R.string.Rs) + selectedVariant.price.toString()

                        if (selectedVariant.discount_percent != "") {
                            val discountPrice = (selectedVariant.discount?.toDouble() ?: 0.0) + (selectedVariant.price?.toDouble() ?: 0.0)
                            tsDiscount.text = discountPrice.toString()
                            tsOferTxt.text = selectedVariant.discount_percent + "%"
                            Glide.with(tsImg)
                                .load(selectedVariant.image.toString())
                                .into(tsImg)
                        } else {
                            sellerDiscountLayout.visibility = View.GONE
                            tsOferImg.visibility = View.GONE
                        }

                        variantPosition = pos
                    }
                }

                Glide.with(tsImg)
                    .load(item.price_variant?.get(0)?.image)
                    .into(tsImg)

                root.setOnClickListener {
                    onClickItem.invoke(item.product_id.toString(), item, false, variantPosition)
                }

                addBtn.setOnClickListener {
                    onClickItem.invoke(item.product_id.toString(), item, true, variantPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = sp
            outRect.right = sp
            outRect.bottom = sp
            outRect.top = sp
        }
    }
}
