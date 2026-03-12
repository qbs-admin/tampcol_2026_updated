package com.qbrains.tampcolapp.ui.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbrains.tampcolapp.data.dbhelper.table.SubCategoriesItemEntity
import com.qbrains.tampcolapp.databinding.ItemCategoryItemBinding

typealias onClickCategory = (categoryItemId: String) -> Unit

class DashboardCategoryAdapter(private val categoryItemId: onClickCategory) :
    RecyclerView.Adapter<DashboardCategoryAdapter.ViewHolder>() {

    private var dashBoardSubCategory = ArrayList<SubCategoriesItemEntity>()

    inner class ViewHolder(private val binding: ItemCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            val item = dashBoardSubCategory[position]
            binding.apply {
                tvItemName.text = item.subCategoryName
                Glide.with(root)
                    .load(item.banner)
                    .into(ivItem)
                root.setOnClickListener {
                    categoryItemId.invoke(item.subCategoryId.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dashBoardSubCategory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUi(position)
    }

    fun addDashBoardCategory(subCategory: List<SubCategoriesItemEntity>) {
        dashBoardSubCategory.clear()
        dashBoardSubCategory.addAll(subCategory)
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
