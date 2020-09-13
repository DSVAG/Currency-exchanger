package com.dsvag.currencyexchanger.data.network

import com.dsvag.currencyexchanger.data.models.latest.Latest
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCoinData {
    @GET("/v1/cryptocurrency/listings/latest")
    fun getCoins(
        @Query("limit") limit: Int = 200,
    ): Single<Latest>
}