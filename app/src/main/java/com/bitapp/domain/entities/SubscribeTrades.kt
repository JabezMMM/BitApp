package com.bitapp.domain.entities

class SubscribeTrades(
        override val event: String,
        override val channel: String,
        override val pair: String
) : BaseSubscribe(event, channel, pair)