package com.dsvag.currencyexchanger.data.models.latest

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Coin(
    @SerializedName("id")
    @PrimaryKey val id: Int,

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

    @SerializedName("tags")
    val tags: List<String>,

    @SerializedName("max_supply")
    val maxSupply: Double,

    @SerializedName("circulating_supply")
    val circulatingSupply: Double,

    @SerializedName("total_supply")
    val totalSupply: Double,

    @SerializedName("platform")
    val platform: Any,

    @SerializedName("cmc_rank")
    val cmcRank: Int,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("quote")
    val quote: Quote,
) {
    fun reprice(usd: Double) {
        quote.usd.priceInAnotherCoin = usd / quote.usd.price
    }
}