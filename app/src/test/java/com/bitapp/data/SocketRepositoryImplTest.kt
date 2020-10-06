package com.bitapp.data

import com.bitapp.data.entity.toSubscribeTickerRequest
import com.bitapp.data.repository.SocketRepositoryImpl
import com.bitapp.domain.entities.SubscribeOrderBook
import com.bitapp.domain.entities.SubscribeTicker
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito

class SocketRepositoryImplTest {
    private lateinit var socketRepositoryImpl: SocketRepositoryImpl

    @Before
    fun before() {
        socketRepositoryImpl = SocketRepositoryImpl(<Retrofit>().create(BitAppRESTApi::class.java))
    }

    @Test
    fun `Given subscribe to ticker, When OnConnectionOpened WebSocket event, Then return expected data`() {
        val subscribeTicker = SubscribeTicker(
            event = "event",
            pair = "pair",
            channel = "channel"
        )

        // response from service
        val tickerArray = arrayOf(
            "chanId",
            "bid",
            "bidSize",
            "ask",
            "askSize",
            "dailyChange",
            "dailyChangePerc",
            "lastPrice",
            "volume",
            "high",
            "low"
        )

        Mockito.`when`(bitfinexClient.subscribeTicker(subscribeTicker.toSubscribeTickerRequest()))
            .thenReturn(Flowable.just(tickerArray))

        socketRepositoryImpl.observeTicker(subscribeTicker).test()
            .assertValue { tickerData ->
                tickerData.chanId == tickerArray[0]
                        &&
                        tickerData.bid == tickerArray[1]
                        &&
                        tickerData.bidSize == tickerArray[2]
                        &&
                        tickerData.ask == tickerArray[3]
                        &&
                        tickerData.askSize == tickerArray[4]
                        &&
                        tickerData.dailyChange == tickerArray[5]
                        &&
                        tickerData.dailyChangePerc == tickerArray[6]
                        &&
                        tickerData.lastPrice == tickerArray[7]
                        &&
                        tickerData.volume == tickerArray[8]
                        &&
                        tickerData.high == tickerArray[9]
                        &&
                        tickerData.low == tickerArray[10]
            }
            .assertComplete()
    }

    @Ignore("unknown nullpointerexception")
    @Test
    fun `Given subscribe to order book, When OnConnectionOpened WebSocket event, Then return expected data`() {
        val subscribeOrderBook = SubscribeOrderBook(
            event = "event",
            pair = "pair",
            channel = "channel",
            frequency = "freq"
        )

        // response from service
        val orderBookArray = doubleArrayOf(
            5770.0, // channel
            5.93, // price
            1.0, // count
            1000.5 // amount
        )

        Mockito.`when`(bitfinexClient.subscribeOrderBook(subscribeOrderBook.toSubcribeOrderBookrRequest()))
            .thenReturn(Flowable.just(orderBookArray))

        socketRepositoryImpl.observeOrderBook(subscribeOrderBook).test()
            .await()
            .assertValue { orderBookData ->
                orderBookData.price == orderBookArray[1] &&
                        orderBookData.count == orderBookArray[2].toInt() &&
                        orderBookData.amount == orderBookArray[3]
            }
            .assertComplete()
    }
}
