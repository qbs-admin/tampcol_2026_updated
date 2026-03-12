package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
import com.qbrains.tampcolapp.databinding.ItemProductListBinding

typealias onClickProduct = (productId: String, priceVarientPosition: Int, move: Boolean, varientId: Int, product: ProductPageEntity) -> Unit

class ProductAdapter(private val onClickProduct: onClickProduct) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var productItem = ArrayList<ProductPageEntity>()
    private var isSearch: Boolean = false

    inner class ViewHolder(private val binding: ItemProductListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val product = productItem[position]
            var priceVarientPosition = 0
            var varientId = 0

            binding.apply {
                pbProductItem.visibility = View.GONE

                // Load image if present
                product.pricevariant.firstOrNull()?.image?.let { imageUrl ->
                    Glide.with(this.root)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivProductImage)
                }

                tvCategoryName.text = product.title

                if (isSearch) {
                    tvAddToCart.visibility = View.GONE
                }

                if (product.deal == "yes" || product.pricevariant.size == 1) {
                    tvSpinner.visibility = View.GONE

                    // Handle price and discount
                    product.pricevariant.firstOrNull()?.let { variant ->
                        tvProductPrice.text = root.context.getString(R.string.Rs) + variant.price.toString()

                        val discountPrice = (variant.discount?.toDouble() ?: 0.0) + (variant.price?.toDouble() ?: 0.0)
                        tvProductSpecialPrice.text = root.context.getString(R.string.Rs) + discountPrice.toString()

                        priceVarientPosition = 0
                        varientId = variant.id?.toInt() ?: 0

                        if (variant.discount_percent.isNullOrEmpty()) {
                            prOferImg.visibility = View.GONE
                        } else {
                            prOferTxt.text = variant.discount_percent + "%"
                        }
                    }

                } else if (product.pricevariant.size > 1) {
                    val spinnerList = product.pricevariant.mapNotNull { it.product_weight }
                    val adapter = ArrayAdapter(root.context, R.layout.spinner_item, spinnerList)

                    tvSpinner.adapter = adapter
                    tvSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            product.pricevariant.getOrNull(position)?.let { variant ->
                                tvProductPrice.text = root.context.getString(R.string.Rs) + variant.price.toString()

                                if (variant.discount_percent.isNullOrEmpty()) {
                                    prOferImg.visibility = View.GONE
                                    pDiscountLayout.visibility = View.GONE
                                } else {
                                    prOferTxt.text = variant.discount_percent + "%"

                                    val discountPrice = (variant.discount?.toDouble() ?: 0.0) + (variant.price?.toDouble() ?: 0.0)
                                    tvProductSpecialPrice.text = root.context.getString(R.string.Rs) + discountPrice.toString()

                                    Glide.with(ivProductImage)
                                        .load(variant.image)
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                        .into(ivProductImage)
                                }

                                priceVarientPosition = position
                                varientId = variant.id?.toInt() ?: 0
                            }
                        }
                    }
                } else {
                    Log.e("TAG", product.deal ?: "Unknown deal")
                }

                tvAddToCart.setOnClickListener {
                    onClickProduct.invoke(
                        product.product_id.toString(),
                        priceVarientPosition,
                        true,
                        varientId,
                        product
                    )
                }

                root.setOnClickListener {
                    onClickProduct.invoke(
                        product.product_id.toString(),
                        priceVarientPosition,
                        false,
                        varientId,
                        product
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = productItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addProduct(products: List<ProductPageEntity>, isSearch: Boolean = false) {
        productItem.clear()
        this.isSearch = isSearch
        productItem.addAll(products)
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
