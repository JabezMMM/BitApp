package com.bitapp.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bitapp.domain.SocketRepository
import com.bitapp.domain.entities.*
import com.bitapp.presentation.model.*
import com.bitapp.presentation.ui.utils.buildOrderBooks
import com.bitapp.presentation.ui.utils.buildTrades
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

class DetailViewModel(private val socketRepository: SocketRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _ticker = MutableLiveData<Ticker>()
    val ticker: LiveData<Ticker> get() = _ticker

    private val _orderBooks = MutableLiveData<List<OrderBook>>()
    val orderBooks: LiveData<List<OrderBook>> get() = _orderBooks

    private val _trades = MutableLiveData<List<Trade>>()
    val trades: LiveData<List<Trade>> get() = _trades

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
    }

    fun subscribeToTradingPair(tradingPair: String) {
        val event: String = BaseSubscribe.SUBSCRIBE_EVENT
        val subscribeTicker = SubscribeTicker(
                event = event,
                channel = BaseSubscribe.TICKER_CHANNEL,
                pair = tradingPair
        )

        compositeDisposable.add(socketRepository.observeTicker(subscribeTicker)
                .subscribeOn(Schedulers.computation())
                .map { tickerData -> tickerData.toTickerModel() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ticker ->
                    _ticker.postValue(ticker)
                }, { e ->
                    Timber.e("observeTickerUseCase error: %s", e.toString())
                })
        )

        val subscribeOrderBook = SubscribeOrderBook(
                event = event,
                channel = BaseSubscribe.ORDER_BOOK_CHANNEL,
                pair = tradingPair,
                frequency = BaseSubscribe.FREQUENCY_ZERO
        )

        compositeDisposable.add(socketRepository.observeOrderBook(subscribeOrderBook)
                .subscribeOn(Schedulers.computation())
                .map { orderBookData -> orderBookData.toOrderBookModel() }
                .map { orderBook -> orderBook.buildOrderBooks() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ orderBookList ->
                    _orderBooks.postValue(ArrayList(orderBookList))
                }, { e ->
                    Timber.e("observeOrderBookUseCase error: %s", e.toString())
                })
        )

        val subscribeTrade = SubscribeTrades(
                event = event,
                channel = BaseSubscribe.TRADE_CHANNEL,
                pair = tradingPair
        )

        compositeDisposable.add(socketRepository.observeTrades(subscribeTrade)
                .subscribeOn(Schedulers.computation())
                .map { tradeData -> tradeData.toTradeModel() }
                .map { trade -> trade.buildTrades() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ tradeList ->
                    _trades.postValue(ArrayList(tradeList))
                }, { e ->
                    Timber.e("observeTradesUseCase error: %s", e.toString())
                })
        )
    }

    fun unSubscribeFromChannels() {
        val event: String = BaseUnsubscribe.UNSUBSCRIBE_EVENT

        _ticker.value?.chanId?.let { channelId ->
            socketRepository.unSubscribeChannel(BaseUnsubscribe(event = event, chanId = channelId))
        }
        _orderBooks.value?.get(0)?.chanId?.let { channelId ->
            socketRepository.unSubscribeChannel(BaseUnsubscribe(event = event, chanId = channelId))
        }
        _trades.value?.get(0)?.chanId?.let { channelId ->
            socketRepository.unSubscribeChannel(BaseUnsubscribe(event = event, chanId = channelId))
        }
    }
}
