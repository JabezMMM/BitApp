package com.bitapp.data.entity

import com.bitapp.domain.entities.SubscribeOrderBook
import com.bitapp.domain.entities.SubscribeTicker
import com.bitapp.domain.entities.SubscribeTrades
import com.squareup.moshi.Json

abstract class BaseSubscribeRequest(
        @Json(name = "event")
        open val event: String,
        @Json(name = "channel")
        open val channel: String,
        @Json(name = "pair")
        open val pair: String
)

data class SubscribeTickerRequest(
        override val event: String,
        override val channel: String,
        override val pair: String
) : BaseSubscribeRequest(event, channel, pair)

fun SubscribeTicker.toSubscribeTickerRequest() =
        SubscribeTickerRequest(
                event = event,
                channel = channel,
                pair = pair
        )

class SubscribeOrderBookRequest(
        override val event: String,
        override val channel: String,
        override val pair: String,
        @Json(name = "freq")
        val frequency: String
) : BaseSubscribeRequest(event, channel, pair)

fun SubscribeOrderBook.toSubscribeOrderBookRequest() =
        SubscribeOrderBookRequest(
                event = event,
                channel = channel,
                pair = pair,
                frequency = frequency
        )

class SubscribeTradesRequest(
        override val event: String,
        override val channel: String,
        override val pair: String
) : BaseSubscribeRequest(event, channel, pair)


fun SubscribeTrades.toSubscribeTradesRequest() =
        SubscribeTradesRequest(
                event = event,
                channel = channel,
                pair = pair
        )

abstract class BaseUnsubscribeRequest(
        @Json(name = "event")
        open val event: String,
        @Json(name = "chanId")
        open val chanId: String
)

