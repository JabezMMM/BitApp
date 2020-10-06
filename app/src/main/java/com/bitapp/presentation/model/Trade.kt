package com.bitapp.presentation.model

import com.bitapp.domain.model.OrderBookData
import com.bitapp.domain.model.TradeData

data class Trade(
        val chanId: String,
        val time: Double,
        val amount: Double,
        val price: Double
)

fun TradeData.toTradeModel() = Trade(
        chanId = chanId.toString(),
        time = time,
        price = price,
        amount = amount
)