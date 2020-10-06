package com.bitapp.presentation.model

import com.bitapp.domain.model.TickerData

data class Ticker(
        val chanId: String,
        var dailyChange: String,
        var lastPrice: String,
        var volume: String,
        var high: String,
        var low: String
)

fun TickerData.toTickerModel() = Ticker(
        chanId = chanId,
        dailyChange = dailyChange,
        lastPrice = lastPrice,
        volume = volume,
        high = high,
        low = low
)