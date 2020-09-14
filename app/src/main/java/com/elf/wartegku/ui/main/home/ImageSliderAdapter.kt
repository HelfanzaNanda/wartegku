package com.elf.wartegku.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.elf.wartegku.R
import com.elf.wartegku.models.Food
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_image_slider.view.*

class ImageSliderAdapter(private val context: Context, private val foods : MutableList<Food>) :
    SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, null))
    }

    override fun getCount(): Int = foods.size

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) = viewHolder!!.bind(foods[position], context)

    class SliderAdapterVH(itemView: View) : ViewHolder(itemView){
        fun bind(food: Food, context: Context){
            with(itemView){ img_food.load(R.drawable.ayam) }
        }
    }

    fun changelist(c : List<Food>){
        foods.clear()
        foods.addAll(c)
        notifyDataSetChanged()
    }
}