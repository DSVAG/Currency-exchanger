package com.dsvag.currencyexchanger.data.repositorys

import com.dsvag.currencyexchanger.data.database.CoinDao
import com.dsvag.currencyexchanger.data.models.dbCoins.Coin
import com.dsvag.currencyexchanger.data.network.ApiCoinData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinRepository(
    private val coinDao: CoinDao,
    private val apiCoinData: ApiCoinData,
) {
    private val TAG = CoinRepository::class.simpleName

    fun getCoins(): Single<List<Coin>> {

        return apiCoinData
            .getCoins(200)
            .subscribeOn(Schedulers.io())
            .flatMap { latest ->
                coinDao.insertAll(latest.coins.map { it.toDbCoin() }).toSingle { latest.coins.map { it.toDbCoin() } }
            }
    }

    fun observeCoins(): Flowable<List<Coin>> {
        return coinDao.getCoins()
    }
}