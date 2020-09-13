package com.dsvag.currencyexchanger.data.repositorys

import android.app.Application
import com.dsvag.currencyexchanger.data.database.CoinDao
import com.dsvag.currencyexchanger.data.database.CoinDatabase
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.models.latest.Latest
import com.dsvag.currencyexchanger.data.network.ApiCoinData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinRepository(application: Application) {
    private val TAG = CoinRepository::class.simpleName

    private var coinDao: CoinDao = CoinDatabase.getDatabase(application).coinDao()

    fun getCoins(): Single<Latest> {
        return ApiCoinData
            .invoke()
            .create(ApiCoinData::class.java)
            .getCoins(200)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addCoins(coins: List<Coin>) {
        Single.just(coins)
            .subscribeOn(Schedulers.io())
            .subscribe({
                coinDao.insertAll(coins)
            }, {

            })
    }

    fun updateCoins(coins: List<Coin>) {
        Single.just(coins)
            .subscribeOn(Schedulers.io())
            .subscribe({
                coinDao.updateAll(coins)
            }, {

            })
    }

    fun deleteCoins(coins: List<Coin>) {
        Single.just(coins)
            .subscribeOn(Schedulers.io())
            .subscribe({
                coinDao.deleteAll(coins)
            }, {

            })
    }

    fun readCoins(): Single<List<Coin>>? {
        return coinDao.getCoins()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}