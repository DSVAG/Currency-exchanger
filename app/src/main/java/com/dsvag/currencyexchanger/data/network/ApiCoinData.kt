package com.dsvag.currencyexchanger.data.network

import com.dsvag.currencyexchanger.data.models.latest.Latest
import io.reactivex.rxjava3.core.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val key = "59716081-0648-4054-8631-56ad6bc21c92"

interface ApiCoinData {
    @GET("/v1/cryptocurrency/listings/latest")
    fun getCoins(
        @Query("limit") limit: Int = 250
    ): Single<Latest>

    companion object {
        operator fun invoke(): Retrofit {
            val requestInterceptor = Interceptor { chain ->

                val url = chain
                    .request()
                    .url()
                    .newBuilder()
                    .build()

                val request = chain
                    .request()
                    .newBuilder()
                    .url(url)
                    .header("X-CMC_PRO_API_KEY", key)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient
                .Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit
                .Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }
    }
}