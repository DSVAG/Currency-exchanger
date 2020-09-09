package com.dsvag.currencyexchanger.data.repositorys

import android.util.Log
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.models.latest.Latest
import com.dsvag.currencyexchanger.data.network.ApiCoinData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinRepository {
    private val TAG = CoinRepository::class.simpleName

    fun apiCall(): List<Coin> {
        val list = ArrayList<Coin>()

        getCoins().subscribe(
            {
                list.addAll(it.coins)
                Log.e(TAG, it.status.errorCode.toString())
            },
            {
                Log.e(TAG, "onError", it)
            }
        )

        return list
    }

    fun getCoins(): Single<Latest> {
        return ApiCoinData
            .invoke()
            .create(ApiCoinData::class.java)
            .getCoins(200)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

//    fun createDb(context: Context) {
//        val db = Room.databaseBuilder(
//            context,
//            AppDatabase::class.java, "database-coins"
//        ).build()
//    }
}