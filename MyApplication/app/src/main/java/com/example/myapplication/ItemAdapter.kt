package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.api.ItemEntity

class ItemAdapter(
    private val onItemClick: (ItemEntity) -> Unit
) : ListAdapter<ItemEntity, ItemAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.itemName)
        val data = view.findViewById<TextView>(R.id.itemData)
    }

    class DiffCallback : DiffUtil.ItemCallback<ItemEntity>() {
        override fun areItemsTheSame(old: ItemEntity, new: ItemEntity) = old.id == new.id
        override fun areContentsTheSame(old: ItemEntity, new: ItemEntity) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.name.text = item.name
        holder.data.text = item.data

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}

