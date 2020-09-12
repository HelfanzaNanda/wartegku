package com.elf.wartegku.ui.main.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.elf.wartegku.R
import com.elf.wartegku.models.Category
import com.elf.wartegku.ui.category.CategoryActivity
import com.elf.wartegku.utils.ext.toast
import kotlinx.android.synthetic.main.item_food_by_category.view.*
import kotlin.time.seconds

class FoodByCategoryAdapter (private var categories: MutableList<Category>, private var context: Context)
    : RecyclerView.Adapter<FoodByCategoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food_by_category, parent, false))
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(categories[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category, context: Context){
            with(itemView){
                txt_category.text = category.category
                if (category.image.equals("picture.jpg")){
                    img_category.load(R.drawable.ayam)
                }else{
                    img_category.load(category.image)
                }
                setOnClickListener {
                    context.startActivity(Intent(context, CategoryActivity::class.java).apply {
                        putExtra("CATEGORY", category)
                    })
                }
            }
        }
    }

    fun changelist(c : List<Category>) {
        categories.clear()
        categories.addAll(c)
        notifyDataSetChanged()
    }
}