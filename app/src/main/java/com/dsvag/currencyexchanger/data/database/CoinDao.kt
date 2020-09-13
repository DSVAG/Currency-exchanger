package com.dsvag.currencyexchanger.data.database

import androidx.room.*
import com.dsvag.currencyexchanger.data.models.latest.Coin
import io.reactivex.rxjava3.core.Single

@Dao
interface CoinDao {
    @Insert
    fun insertAll(coins: List<Coin>)

    @Update
    fun updateAll(coins: List<Coin>)

    @Delete
    fun deleteAll(coins: List<Coin>)

    @Query("SELECT * FROM coin_data")
    fun getCoins(): Single<List<Coin>>
}