package com.qbrains.tampcolapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.qbrains.tampcolapp.databinding.GroupItemBinding
import com.qbrains.tampcolapp.databinding.ItemTextviewBinding

typealias onClickExpandable = (itemPosition: Int) -> Unit

class MyExpandableListAdapter(
    private val context: Context,
    private val group: ArrayList<String>,
    private val collection: Map<String, List<String>>,
    private val listener: onClickExpandable
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return collection.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return collection[group[groupPosition]]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return group[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return collection[group[groupPosition]]?.get(childPosition) ?: ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding = convertView?.let { GroupItemBinding.bind(it) }
            ?: GroupItemBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.grpItemCategry.text = getGroup(groupPosition) as String

        val arrow = binding.upArrowImg
        if (isExpanded) {
            arrow.rotation = 180f
        } else {
            arrow.rotation = 0f
        }

        return binding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding = convertView?.let { ItemTextviewBinding.bind(it) }
            ?: ItemTextviewBinding.inflate(LayoutInflater.from(context), parent, false)

        val itemText = getChild(groupPosition, childPosition) as String
        binding.itemTxt.text = itemText
        binding.itemTxt.setOnClickListener {
            listener.invoke(childPosition)
        }

        return binding.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
