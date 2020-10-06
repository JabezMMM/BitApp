package com.bitapp.presentation.ui.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bitapp.R
import com.bitapp.databinding.TradeItemBinding
import com.bitapp.presentation.model.Trade
import java.text.DecimalFormat
import java.util.*

class TradesAdapter : ListAdapter<Trade, TradesAdapter.OrderBookViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Trade>() {

        override fun areItemsTheSame(oldItem: Trade, newItem: Trade) = when {
            else -> oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Trade, newItem: Trade) = oldItem.time == newItem.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OrderBookViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))

    override fun getItemViewType(position: Int): Int {
        return R.layout.trade_item
    }

    override fun onBindViewHolder(viewHolder: OrderBookViewHolder, position: Int) {
        viewHolder.bindOrderBookItems(getItem(position))
    }

    class OrderBookViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bindOrderBookItems(order: Trade) = with(TradeItemBinding.bind(view)) {
            val decimalFormat = DecimalFormat(view.context.getString(R.string.number_format))

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = calendar.timeInMillis - order.time.toLong()
            time.text = DateFormat.format("hh:mm:ss", calendar.time) as String

            amount.text = decimalFormat.format(order.amount)
            price.text = decimalFormat.format(order.price)
        }
    }
}
