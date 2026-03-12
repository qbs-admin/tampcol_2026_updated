package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.data.dbhelper.table.CategoriesItemEntity
import com.qbrains.tampcolapp.databinding.ItemCategoryItemBinding

typealias onClikCategory = (categoryItemId: String) -> Unit

class CategoryAdapter(private val onClikCategory: onClikCategory) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var categories = ArrayList<CategoriesItemEntity>()

    inner class ViewHolder(private val binding: ItemCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            binding.apply {
                val category = categories[position]
                tvItemName.text = category.categoryName
                Glide.with(root)
                    .load(category.banner)
                    .into(ivItem)
                root.setOnClickListener {
                    onClikCategory.invoke(category.categoryId.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addDashBoardCategory(subCategory: List<CategoriesItemEntity>) {
        categories.clear()
        categories.addAll(subCategory)
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
