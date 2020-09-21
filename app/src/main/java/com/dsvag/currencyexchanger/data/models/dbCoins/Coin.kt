package com.dsvag.currencyexchanger.data.models.dbCoins

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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

    @SerializedName("price")
    val price: Double,

    var priceInAnotherCoin: Double = 0.0,
) {
    fun reprice(usd: Double) {
        priceInAnotherCoin = usd / price
    }
}