package com.bitapp.presentation

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.Assert.assertEquals

import io.reactivex.Flowable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import com.bitapp.RxImmediateSchedulerRule
import com.bitapp.domain.model.OrderBookData
import com.bitapp.domain.model.TickerData
import com.bitapp.domain.usecase.ObserveOrderBookUseCase
import com.bitapp.domain.usecase.ObserveTickerUseCase
import com.bitapp.presentation.ui.viewmodel.DetailViewModel
import com.bitapp.presentation.model.toTickerModel
import com.bitapp.presentation.model.toOrderBookModel

class DetailViewModelTest {

    private lateinit var detailViewModel: DetailViewModel
    private val mockObserveTickerUseCase: ObserveTickerUseCase = mock()
    private val mockObserveOrderBookUseCase: ObserveOrderBookUseCase = mock()

    private val tickerData =
        TickerData(
            "01",
            "1",
            "101",
            "2",
            "201",
            "3",
            "4",
            "5",
            "1000",
            "500",
            "50"
            )

    private val orderBookData1 = OrderBookData(50.5, 5, 100.5)
    private val orderBookData2 = OrderBookData(100.5, 10, 50.5)

    @Rule
    @JvmField
    val rxSchedulersOverrideRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `get ticker and orderbook data succeeds`() {
        // given
        whenever(mockObserveTickerUseCase.invoke()).thenReturn(Flowable.just(tickerData))
        whenever(mockObserveOrderBookUseCase.invoke()).thenReturn(Flowable.just(orderBookData1, orderBookData2))
        // when
        detailViewModel = DetailViewModel(mockObserveTickerUseCase, mockObserveOrderBookUseCase)
        // then
        verify(mockObserveTickerUseCase).invoke()
        verify(mockObserveOrderBookUseCase).invoke()
        assertEquals(detailViewModel.ticker.value, tickerData.toTickerModel())
        assertEquals(detailViewModel.orderBooks.value, listOf(orderBookData1, orderBookData2).map { orderBookData -> orderBookData.toOrderBookModel() })
    }
}