package com.bitapp.presentation.model

import com.bitapp.domain.model.OrderBookData

data class OrderBook(
        val chanId: String,
        val amount: Double,
        val count: Int,
        val price: Double
)

fun OrderBookData.toOrderBookModel() = OrderBook(
        chanId = chanId.toString(),
        price = price,
        count = count,
        amount = amount
)