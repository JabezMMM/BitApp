package com.bitapp.data.remote

import retrofit2.http.GET

interface BitRestApi {
    @GET("conf/pub:list:pair:exchange")
    suspend fun getTradingPairs(): List<List<String>>

    @GET("conf/pub:list:currency")
    suspend fun getCurrencyList(): List<List<String>>

    companion object {
        const val BASE_URL = "https://api-pub.bitfinex.com/v2/"
    }
}