package com.bitapp.data.remote

import com.bitapp.data.entity.*
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface BitSocketApi {

    @Receive
    fun openWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun receiveResponse(): Flowable<JsonResponse>

    @Send
    fun sendTickerRequest(subscribeTickerRequest: SubscribeTickerRequest)

    @Receive
    fun observeTicker(): Flowable<Array<String>>

    @Send
    fun sendOrderBookRequest(subscribeOrderBookRequest: SubscribeOrderBookRequest)

    @Receive
    fun observeOrderBook(): Flowable<DoubleArray>

    @Send
    fun sendTradeRequest(subscribeOrderBookRequest: SubscribeTradesRequest)

    @Receive
    fun observeTrades(): Flowable<DoubleArray>

    @Send
    fun sendUnsubscribeRequest(unsubscribeRequest: UnsubscribeRequest)

    companion object {
        const val BASE_URI = "wss://api.bitfinex.com/ws/"
    }
}