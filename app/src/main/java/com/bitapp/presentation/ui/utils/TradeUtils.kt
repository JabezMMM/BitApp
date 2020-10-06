package com.bitapp.presentation.ui.utils

import com.bitapp.presentation.model.Trade
import java.util.*

private const val LIST_SIZE = 10
private val tradesByTime: TreeMap<Double, Trade> = TreeMap()

fun Trade.buildTrades(): List<Trade> {
    tradesByTime[price] = this

    val tradeList: ArrayList<Trade> = arrayListOf()
    tradesByTime.forEach { (_, trade) ->
        tradeList.add(trade)
    }

    return tradeList
}

fun getTradeList(list: List<Trade>): List<Trade> {
    return list.sortedBy { it.time }.reversed().take(LIST_SIZE)
}