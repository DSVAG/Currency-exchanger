package com.dsvag.currencyexchanger.data.repositorys

import com.dsvag.currencyexchanger.data.database.CoinDao
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.network.ApiCoinData
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
            .flatMap {
                coinDao.insertAll(it.coins).toSingle { it.coins }
            }
            .onErrorResumeNext {
                coinDao.getCoins()
            }
    }
}