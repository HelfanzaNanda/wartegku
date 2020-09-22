package com.elf.wartegku.ui.checkout

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elf.wartegku.R
import com.elf.wartegku.models.Food
import com.elf.wartegku.utils.Constants
import kotlinx.android.synthetic.main.item_checkout.view.*

class CheckoutAdapter (private val foods : MutableList<Food>)
    : RecyclerView.Adapter<CheckoutAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(food: Food) {
            with(itemView){
                txt_name.text = food.name
                txt_price.text = Constants.setToIDR(food.price!!)
                txt_total_price.text = Constants.setToIDR(food.price!!.times(food.qty!!))
                txt_qty.text = "${food.qty}x"
            }
        }
    }

    fun changelist(c : List<Food>){
        foods.clear()
        foods.addAll(c)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_checkout, parent, false))
    }

    override fun getItemCount(): Int = foods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(foods[position])
}