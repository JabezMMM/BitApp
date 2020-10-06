package com.bitapp.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bitapp.domain.RestRepository

class ListViewModel(private val restRepository: RestRepository) : ViewModel() {
    val tradingPairs = liveData {
        requestData {
            restRepository.getTradingPairs()
        }?.let {
            emit(it)
        }
    }

    val currencyList = liveData {
        requestData {
            restRepository.getCurrencyList()
        }?.let {
            emit(it)
        }
    }

    private inline fun <reified ResponseType> requestData(request: () -> ResponseType): ResponseType? {
        return request.invoke()
    }
}