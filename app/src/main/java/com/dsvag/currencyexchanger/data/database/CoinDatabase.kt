package com.dsvag.currencyexchanger.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.utils.Converters

@Database(entities = [Coin::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao

    companion object {
        @Volatile
        private var INSTANCE: CoinDatabase? = null

        fun getDatabase(context: Context): CoinDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    CoinDatabase::class.java, "database-coins"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}