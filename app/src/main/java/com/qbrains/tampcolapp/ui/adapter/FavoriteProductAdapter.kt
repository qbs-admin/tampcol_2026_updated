package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.reponse.ProductItem
import com.qbrains.tampcolapp.databinding.ItemFavoriteProductsBinding

typealias onClickFavorite = (ProductId: String, delete: Boolean, position: Int, item: ProductItem) -> Unit

class FavoriteProductAdapter(private val productId: onClickFavorite) : RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder>() {

    private var mProductItem = ArrayList<ProductItem>()
    private val imgBaseUrl = "https://harinafoods.com/uploads/product_image/"

    inner class ViewHolder(private val binding: ItemFavoriteProductsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = mProductItem[position]
            binding.apply {
                pbFProductItem.visibility = View.GONE
                tvFavWeight.text = item.price_variant!![0].product_weight
                Glide.with(root)
                    .load(item.price_variant!![0].image.toString())
                    .into(ivFProductImage)
                tvFCategoryName.text = item.title

                if (item.price_variant!![0].discount_percent!!.isEmpty()) {
                    oferImg.visibility = View.GONE
                } else {
                    oferTxt.text = item.price_variant!![0].discount_percent + "%"
                }

                if (item.price_variant!![0].discount?.toString() != "") {
                    val discountPrice = item.price_variant!![0].discount?.toDouble()!! +
                            item.price_variant!![0].price?.toDouble()!!
                    tvFProductSpecialPrice.text = root.context.getString(R.string.Rs) + discountPrice.toString()
                } else {
                    favDiscountLayout.visibility = View.GONE
                }

                tvFProductPrice.text = root.context.getString(R.string.Rs) + item.price_variant!![0].price.toString()

                favAddToCart.setOnClickListener {
                    productId.invoke(item.product_id.toString(), false, position, item)
                }

                delwishlistBtn.setOnClickListener {
                    productId.invoke(item.product_id.toString(), true, position, item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mProductItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addProduct(productItem: List<ProductItem>) {
        mProductItem.clear()
        mProductItem.addAll(productItem)
        notifyDataSetChanged()
    }

    fun delProduct(productItem: List<ProductItem>, position: Int) {
        mProductItem.removeAt(position)
        notifyDataSetChanged()
    }

    class ItemCategoryAdapterDecoration(private val sp: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = sp
            outRect.right = sp
            outRect.bottom = sp
            outRect.top = sp
        }
    }
}
