package com.dsvag.currencyexchanger.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dsvag.currencyexchanger.data.models.latest.Coin
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(coins: List<Coin>): Completable

    @Delete
    fun deleteAll(coins: List<Coin>): Completable

    @Query("SELECT * FROM coin")
    fun getCoins(): Single<List<Coin>>
}