package com.dsvag.currencyexchanger.data.utils

import android.app.Application
import androidx.room.Room
import com.dsvag.currencyexchanger.data.database.CoinDao
import com.dsvag.currencyexchanger.data.database.CoinDatabase
import com.dsvag.currencyexchanger.data.network.ApiCoinData
import com.dsvag.currencyexchanger.data.repositorys.CoinRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val key = "59716081-0648-4054-8631-56ad6bc21c92"

class AppComponent(application: Application) {

    private val database by lazy {
        Room.databaseBuilder(
            application,
            CoinDatabase::class.java, "database-coins"
        ).build()
    }

    private val coinDao: CoinDao by lazy { database.coinDao() }

    private val requestInterceptor by lazy {

        Interceptor { chain ->

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

            chain.proceed(request)
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://pro-api.coinmarketcap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val apiCoinData: ApiCoinData by lazy {
        retrofit.create(ApiCoinData::class.java)
    }

    val keyBoardUtils by lazy { KeyBoardUtils(application) }

    val coinRepository by lazy { CoinRepository(coinDao, apiCoinData) }
}