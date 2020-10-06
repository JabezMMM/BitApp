package com.bitapp.domain

interface RestRepository {
    suspend fun getTradingPairs(): List<String>
    suspend fun getCurrencyList(): List<String>
}