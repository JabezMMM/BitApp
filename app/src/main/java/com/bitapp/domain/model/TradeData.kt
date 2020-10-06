package com.bitapp.domain.model

data class TradeData(
        val chanId: Double,
        val time: Double,
        val price: Double,
        val amount: Double
)

fun DoubleArray.toTradeData() = TradeData(
        chanId = this[0],
        time = this[0],
        price = this[1],
        amount = this[3]
)
