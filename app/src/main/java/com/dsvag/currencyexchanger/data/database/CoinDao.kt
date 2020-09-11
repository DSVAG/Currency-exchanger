package com.dsvag.currencyexchanger.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dsvag.currencyexchanger.data.models.latest.Coin

@Dao
interface CoinDao {
    @Insert
    fun insertAll(vararg coin: Coin)

    @Delete
    fun deleteAll(vararg coin: Coin)

    @Query("SELECT * FROM Coin")
    fun getCoins(): List<Coin>
}