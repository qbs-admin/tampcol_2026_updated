package com.qbrains.tampcolapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qbrains.tampcolapp.data.network.api.reponse.SlidesItem
import com.qbrains.tampcolapp.databinding.SliderLayoutBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter : SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {

    private var slidesItem = ArrayList<SlidesItem>()

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterViewHolder {
        val binding = SliderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
        viewHolder.bindUi(position)
    }

    override fun getCount(): Int = slidesItem.size

    fun addSlides(slides: List<SlidesItem>) {
        Log.e("slides", slides.toString())
        slidesItem.clear()
        slidesItem.addAll(slides)
        notifyDataSetChanged()
    }

    inner class SliderAdapterViewHolder(private val binding: SliderLayoutBinding) : ViewHolder(binding.root) {
        fun bindUi(position: Int) {
            binding.apply {
                Glide.with(root)
                    .load(slidesItem[position].images)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageSlider)
            }
        }
    }
}
