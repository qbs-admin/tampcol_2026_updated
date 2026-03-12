package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.network.api.reponse.Product
import com.qbrains.tampcolapp.databinding.ItemCardViewBinding

typealias onCartNew = (ProductQty: Int, ProductPrice: Double, ProductId: String, delete: Boolean, priceId: String) -> Unit

class CartViewAdapterNew(var onCart: onCartNew) :
    RecyclerView.Adapter<CartViewAdapterNew.ViewHolder>() {
    var mProductItem = ArrayList<Product>()

    inner class ViewHolder(private val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            binding.apply {
                // Hide progress bar
                pbCartProduct.visibility = View.GONE

                // Load image with Glide
                Glide.with(binding.root)
                    .load(mProductItem[position].priceVariant[0].image)
                    .into(ivCartProductImage)

                // Set text fields
                tvCartWeight.text = "Weight : ${mProductItem[position].priceVariant[0].productWeight}"
                tvCartWeight.visibility = View.GONE
                tvCartCategoryName.text = mProductItem[position].title
                val totalPrice = mProductItem[position].priceVariant[0].price
                tvCartCategoryPrice.text = binding.root.context.getString(R.string.Rs) + ("%.2f".format(totalPrice * (mProductItem[position].priceVariant[0].quantity)).toDouble()).toString()
                tvProductQty.text = mProductItem[position].priceVariant[0].quantity.toString()

                // Handle button clicks
                tvAddProduct.setOnClickListener {
                    onCart.invoke(
                        mProductItem[position].priceVariant[0].quantity + 1,
                        mProductItem[position].priceVariant[0].price * (mProductItem[position].priceVariant[0].quantity + 1),
                        mProductItem[position].productId,
                        false,
                        mProductItem[position].priceVariant[0].id
                    )
                    tvProductQty.text = (mProductItem[position].priceVariant[0].quantity + 1).toString()
                }

                tvReduceProduct.setOnClickListener {
                    if (mProductItem[position].priceVariant[0].quantity == 1) {
                        onCart.invoke(
                            0,
                            0.0,
                            mProductItem[position].productId,
                            true,
                            mProductItem[position].priceVariant[0].id
                        )
                    } else {
                        onCart.invoke(
                            mProductItem[position].priceVariant[0].quantity - 1,
                            mProductItem[position].priceVariant[0].price * (mProductItem[position].priceVariant[0].quantity - 1),
                            mProductItem[position].productId,
                            false,
                            mProductItem[position].priceVariant[0].id
                        )
                        tvProductQty.text = (mProductItem[position].priceVariant[0].quantity - 1).toString()
                    }
                }

                ivCartDelete.setOnClickListener {
                    onCart.invoke(
                        0,
                        0.0,
                        mProductItem[position].productId,
                        true,
                        mProductItem[position].priceVariant[0].id
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mProductItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addCartItem(cartItems: List<Product>) {
        mProductItem.clear()
        mProductItem.addAll(cartItems)
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
            space = sp
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space
        }
    }
}
