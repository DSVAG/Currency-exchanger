package com.dsvag.currencyexchanger.data.models.latest

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dsvag.currencyexchanger.data.utils.Converters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "coin_data")
data class Coin(
    @SerializedName("id")
    @PrimaryKey val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("num_market_pairs")
    val numMarketPairs: Int,

    @SerializedName("date_added")
    val dateAdded: String,

    @TypeConverters(Converters::class)
    @SerializedName("tags")
    val tags: List<String>,

    @SerializedName("max_supply")
    val maxSupply: Double,

    @SerializedName("circulating_supply")
    val circulatingSupply: Double,

    @SerializedName("total_supply")
    val totalSupply: Double,

    @TypeConverters(Converters::class)
    @SerializedName("platform")
    val platform: Platform?,

    @SerializedName("cmc_rank")
    val cmcRank: Int,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @TypeConverters(Converters::class)
    @SerializedName("quote")
    val quote: Quote,
) {
    fun reprice(usd: Double) {
        quote.usd.priceInAnotherCoin = usd / quote.usd.price
    }
}