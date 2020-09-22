package com.elf.wartegku.ui.detail_store

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.elf.wartegku.R
import com.elf.wartegku.models.Food
import com.elf.wartegku.utils.Constants
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.visible
import kotlinx.android.synthetic.main.item_food.view.*

class FoodAdapter (private var foods : MutableList<Food>,
                   private var foodClickInterface: FoodClickInterface
)
    :RecyclerView.Adapter<FoodAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false))
    }

    override fun getItemCount(): Int = foods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(foods[position])

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(food: Food) {
            with(itemView){
                txt_name.text = food.name
                txt_desc.text = food.description
                txt_price.text = Constants.setToIDR(food.price!!)
                img_food.load(R.drawable.ayam)
                if (food.qty!! > 0){
                    txt_qty.text = food.qty.toString()
                    btn_plus.gone()
                    linear_calc.visible()
                    btn_inc.setOnClickListener { foodClickInterface.increment(food) }
                    btn_dec.setOnClickListener { foodClickInterface.decrement(food) }
                }else{
                    btn_plus.visible()
                    btn_plus.setOnClickListener { foodClickInterface.add(food) }
                    linear_calc.gone()
                }
            }
        }
    }

    fun changelist(c : List<Food>){
        foods.clear()
        foods.addAll(c)
        notifyDataSetChanged()
    }
}