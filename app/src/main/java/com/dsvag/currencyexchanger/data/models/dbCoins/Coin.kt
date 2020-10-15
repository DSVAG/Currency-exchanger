package com.dsvag.currencyexchanger.data.models.dbCoins

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dsvag.currencyexchanger.data.utils.Converter
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Entity(tableName = "coin")
data class Coin(
    @SerializedName("id")
    @PrimaryKey val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @TypeConverters(Converter::class)
    @SerializedName("price")
    val price: BigDecimal,

    val priceInAnotherCoin: BigDecimal = BigDecimal.ZERO,
)