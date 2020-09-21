package com.dsvag.currencyexchanger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dsvag.currencyexchanger.data.models.dbCoins.Coin

@Database(entities = [Coin::class], version = 1, exportSchema = false)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}