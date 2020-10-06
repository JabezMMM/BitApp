package com.bitapp.data.repository

import android.annotation.SuppressLint
import com.bitapp.data.entity.toSubscribeOrderBookRequest
import com.bitapp.data.entity.toSubscribeTickerRequest
import com.bitapp.data.entity.toSubscribeTradesRequest
import com.bitapp.data.entity.toUnsubscribeRequest
import com.bitapp.data.remote.BitSocketApi
import com.bitapp.domain.SocketRepository
import com.bitapp.domain.entities.BaseUnsubscribe
import com.bitapp.domain.entities.SubscribeOrderBook
import com.bitapp.domain.entities.SubscribeTicker
import com.bitapp.domain.entities.SubscribeTrades
import com.bitapp.domain.model.*
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

private const val TICKER_SNAPSHOT_SIZE = 11 // https://docs.bitfinex.com/reference#ws-public-ticker
private const val ORDER_BOOK_SNAPSHOT_SIZE = 4
private const val TRADES_SNAPSHOT_SIZE = 4

@SuppressLint("CheckResult")
class SocketRepositoryImpl(private val bitSocketApi: BitSocketApi) : SocketRepository {

    override fun observeTicker(subscribeTicker: SubscribeTicker): Flowable<TickerData> {
        bitSocketApi.openWebSocketEvent()
                .filter { it is WebSocket.Event.OnConnectionOpened<*> }
                .subscribe({
                    bitSocketApi.sendTickerRequest(subscribeTicker.toSubscribeTickerRequest())
                }, { e -> Timber.e(e) })

        return bitSocketApi.observeTicker()
                .subscribeOn(Schedulers.io())
                .filter { it.size == TICKER_SNAPSHOT_SIZE } // make sure it's a ticker
                .map { response -> response.toTickerData() }
    }

    override fun observeOrderBook(subscribeOrderBook: SubscribeOrderBook): Flowable<OrderBookData> {
        bitSocketApi.openWebSocketEvent()
                .filter { it is WebSocket.Event.OnConnectionOpened<*> }
                .subscribe({
                    bitSocketApi.sendOrderBookRequest(subscribeOrderBook.toSubscribeOrderBookRequest())
                }, { e -> Timber.e(e) })

        return bitSocketApi.observeOrderBook()
                .subscribeOn(Schedulers.io())
                .filter { it.size == ORDER_BOOK_SNAPSHOT_SIZE } // make sure it's an order book
                .map { response -> response.toOrderBookData() }

    }

    override fun observeTrades(subscribeOrderBook: SubscribeTrades): Flowable<TradeData> {
        bitSocketApi.openWebSocketEvent()
                .filter { it is WebSocket.Event.OnConnectionOpened<*> }
                .subscribe({
                    bitSocketApi.sendTradeRequest(subscribeOrderBook.toSubscribeTradesRequest())
                }, { e -> Timber.e(e) })

        return bitSocketApi.observeTrades()
                .subscribeOn(Schedulers.io())
                .filter { it.size == TRADES_SNAPSHOT_SIZE } // make sure it's a trade
                .map { response -> response.toTradeData() }
    }

    override fun unSubscribeChannel(unsubscribe: BaseUnsubscribe) {
        // bitSocketApi.sendUnsubscribeRequest(unsubscribe.toUnsubscribeRequest())
    }
}
