package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.MyCartEntity
import com.qbrains.tampcolapp.databinding.ItemCardViewBinding

typealias onCart = (ProductQty: Int, ProductPrice: Double, ProductId: String, delete: Boolean, priceId: String) -> Unit

class CartViewAdapter(private val onCart: onCart) : RecyclerView.Adapter<CartViewAdapter.ViewHolder>() {
    var mProductItem = ArrayList<MyCartEntity>()
    private var db: LaaFreshDAO? = null
    private var myCartEntity: MyCartEntity? = null

    inner class ViewHolder(private val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = mProductItem[position]
            db = LaaFreshDB.getInstance(context = binding.root.context)?.laaFreshDAO()

            binding.apply {
                pbCartProduct.visibility = View.GONE
                Glide.with(binding.root.context)
                    .load(item.productURI)
                    .into(ivCartProductImage)
                tvCartWeight.text = item.productWight
                tvCartCategoryName.text = item.productName
                val totalPrice = item.orderValue
                tvCartCategoryPrice.text = binding.root.context.getString(R.string.Rs) + ("%.2f".format(totalPrice))
                tvProductQty.text = item.orderQty.toString()

                tvAddProduct.setOnClickListener {
                    myCartEntity = db?.checkItemAlreadyExist(item.priceId)
                    if (myCartEntity?.orderQty ?: 0 >= 1) {
                        onCart.invoke(
                            item.orderQty + 1,
                            item.subtotal * (item.orderQty + 1),
                            item.productCode,
                            false,
                            item.priceId
                        )
                    }
                    tvProductQty.text = (item.orderQty + 1).toString()
                    tvCartCategoryPrice.text = binding.root.context.getString(R.string.Rs) + ("%.2f".format(totalPrice * (item.orderQty + 1)))
                }

                tvReduceProduct.setOnClickListener {
                    myCartEntity = db?.checkItemAlreadyExist(item.priceId)
                    if (myCartEntity?.orderQty == 1) {
                        onCart.invoke(
                            0,
                            0.0,
                            item.productCode,
                            true,
                            item.priceId
                        )
                    } else {
                        onCart.invoke(
                            item.orderQty - 1,
                            item.subtotal * (item.orderQty - 1),
                            item.productCode,
                            false,
                            item.priceId
                        )
                        tvProductQty.text = (item.orderQty - 1).toString()
                        tvCartCategoryPrice.text = binding.root.context.getString(R.string.Rs) + ("%.2f".format(totalPrice * (item.orderQty - 1)))
                    }
                }

                ivCartDelete.setOnClickListener {
                    myCartEntity = db?.checkItemAlreadyExist(item.priceId)
                    if (myCartEntity?.orderQty ?: 0 >= 1) {
                        onCart.invoke(
                            0,
                            0.0,
                            item.productCode,
                            true,
                            item.priceId
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mProductItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addCartItem(cartItems: ArrayList<MyCartEntity>) {
        mProductItem.clear()
        mProductItem.addAll(cartItems)
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
