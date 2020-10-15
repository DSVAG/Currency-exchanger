package com.dsvag.currencyexchanger.data.models.requestCoins

import com.dsvag.currencyexchanger.data.models.dbCoins.Coin
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class RequestCoin(
    @SerializedName("id")
    val id: Long,

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
    val platform: Platform?,

    @SerializedName("cmc_rank")
    val cmcRank: Int,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("quote")
    val quote: Quote,
) {

    fun toDbCoin(): Coin {
        return Coin(
            id,
            name,
            symbol,
            slug,
            quote.usd.lastUpdated.split("T")[1].replace(".000Z", ""),
            BigDecimal(quote.usd.price)
        )
    }
}