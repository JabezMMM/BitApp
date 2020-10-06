package com.bitapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitapp.R
import kotlinx.android.synthetic.main.list_item_text.view.*

class SimpleListAdapter(private val itemClickListener: ItemClickListener<String>) : ListAdapter<String, ViewHolder>(ListItemDiffCallback()) {
    @LayoutRes
    var itemLayout: Int = R.layout.list_item_text

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)
        holder.itemView.title.text = option

        holder.itemView.setOnClickListener {
            itemClickListener.onItemSelected(option)
        }
    }
}

private class ListItemDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

interface ItemClickListener<ItemType> {

    fun onItemSelected(item: ItemType)
}