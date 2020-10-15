package com.dsvag.currencyexchanger.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dsvag.currencyexchanger.data.models.dbCoins.Coin
import com.dsvag.currencyexchanger.data.utils.Converter

@Database(entities = [Coin::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}