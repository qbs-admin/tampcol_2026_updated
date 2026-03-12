package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.reponse.ProductsItems
import com.qbrains.tampcolapp.databinding.ItemProductBundleListBinding

typealias onClickBundleProduct = (productId: String, move: Boolean) -> Unit

class ProductBundleAdapter(private val onClickBundleProduct: onClickBundleProduct) :
    RecyclerView.Adapter<ProductBundleAdapter.ViewHolder>() {

    private var mProductItem = ArrayList<ProductsItems>()

    inner class ViewHolder(private val binding: ItemProductBundleListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val product = mProductItem[position]

            binding.apply {
                pbBundleProductItem.visibility = View.GONE

                Glide.with(this.root)
                    .load(product.image)
                    .into(ivBundleProductImage)

                tvBundleCategoryName.text = product.title
                tvBundleProductPrice.text = root.context.getString(R.string.Rs) + " " + product.salePrice

                if (product.currentStock == "0") {
                    tvBundleAddToCart.visibility = View.GONE
                    linearBundleProductQty.visibility = View.GONE
                    tvBundleNoStock.visibility = View.VISIBLE
                } else {
                    tvBundleAddToCart.visibility = View.VISIBLE
                    linearBundleProductQty.visibility = View.VISIBLE
                    tvBundleNoStock.visibility = View.GONE
                }

                tvBundleAddToCart.setOnClickListener {
                    onClickBundleProduct.invoke(product.productId.toString(), true)
                }

                root.setOnClickListener {
                    onClickBundleProduct.invoke(product.productId.toString(), false)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBundleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mProductItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addProduct(productItem: List<ProductsItems>) {
        mProductItem.clear()
        mProductItem.addAll(productItem)
        notifyDataSetChanged()
    }

    class ItemCategoryAdapterDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}
