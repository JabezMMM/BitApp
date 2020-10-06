package com.bitapp.domain.model

data class OrderBookData(
        val chanId: Double,
        val price: Double,
        val count: Int,
        val amount: Double
)

fun DoubleArray.toOrderBookData() = OrderBookData(
        chanId = this[0],
        price = this[1],
        count = this[2].toInt(),
        amount = this[3]
)
