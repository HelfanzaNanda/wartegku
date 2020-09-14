package com.elf.wartegku.ui.category.store

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.elf.wartegku.R
import com.elf.wartegku.models.Store
import com.elf.wartegku.ui.food.detail_store.DetailStoreActivity
import kotlinx.android.synthetic.main.item_store.view.*

class StoreAdapter (private var stores : MutableList<Store>, private var context: Context)
    : RecyclerView.Adapter<StoreAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false))
    }

    override fun getItemCount(): Int = stores.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(stores[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(store: Store, context: Context){
            with(itemView){
                txt_store.text = store.name
                if (store.logo.isNullOrEmpty()){
                    img_store.load(R.drawable.store)
                }
                setOnClickListener {
                    context.startActivity(Intent(context, DetailStoreActivity::class.java).apply {
                        putExtra("STORE", store)
                    })
                }
            }
        }
    }

    fun changelist(c : List<Store>){
        stores.clear()
        stores.addAll(c)
        notifyDataSetChanged()
    }
}