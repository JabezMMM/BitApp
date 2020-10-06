package com.bitapp.domain.entities

abstract class BaseSubscribe(
        open val event: String,
        open val channel: String,
        open val pair: String
) {
    companion object {
        const val SUBSCRIBE_EVENT = "subscribe"
        const val TICKER_CHANNEL = "ticker"
        const val ORDER_BOOK_CHANNEL = "book"
        const val TRADE_CHANNEL = "trades"
        const val FREQUENCY_ZERO = "F0"
        const val FREQUENCY_ONE = "F1"
    }
}

