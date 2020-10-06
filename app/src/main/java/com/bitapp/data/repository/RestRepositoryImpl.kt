package com.bitapp.data.repository

import android.util.Log
import com.bitapp.data.remote.BitRestApi
import com.bitapp.domain.RestRepository

private const val TAG = "TradingPairRepositoryIm"

class RestRepositoryImpl(private val bitRestApi: BitRestApi) : RestRepository {
    override suspend fun getTradingPairs(): List<String> {
        return try {
            with(bitRestApi.getTradingPairs()) {
                this.first()
            }
        } catch (throwable: Throwable) {
            Log.e(TAG, "getTradingPairs: $throwable")
            emptyList()
        }
    }

    override suspend fun getCurrencyList(): List<String> {
        return try {
            with(bitRestApi.getCurrencyList()) {
                this.first()
            }
        } catch (throwable: Throwable) {
            Log.e(TAG, "getCurrencyList: $throwable")
            emptyList()
        }
    }
}