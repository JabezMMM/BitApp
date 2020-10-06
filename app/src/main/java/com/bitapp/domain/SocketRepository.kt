package com.bitapp.domain

import com.bitapp.domain.entities.BaseUnsubscribe
import com.bitapp.domain.entities.SubscribeOrderBook
import com.bitapp.domain.entities.SubscribeTicker
import com.bitapp.domain.entities.SubscribeTrades
import com.bitapp.domain.model.OrderBookData
import com.bitapp.domain.model.TradeData
import com.bitapp.domain.model.TickerData
import io.reactivex.Flowable

interface SocketRepository {

    fun observeTicker(subscribeTicker: SubscribeTicker): Flowable<TickerData>

    fun observeOrderBook(subscribeOrderBook: SubscribeOrderBook): Flowable<OrderBookData>

    fun observeTrades(subscribeOrderBook: SubscribeTrades): Flowable<TradeData>

    fun unSubscribeChannel(unsubscribe: BaseUnsubscribe)
}

