package com.dsvag.currencyexchanger.data.models.latest


import com.google.gson.annotations.SerializedName

data class Coin(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerializedName("num_market_pairs")
    val numMarketPairs: Int,
    @SerializedName("date_added")
    val dateAdded: String,
    val tags: List<String>,
    @SerializedName("max_supply")
    val maxSupply: Double,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("total_supply")
    val totalSupply: Double,
    val platform: Any,
    @SerializedName("cmc_rank")
    val cmcRank: Int,
    @SerializedName("last_updated")
    val lastUpdated: String,
    val quote: Quote,
    var isExpand: Boolean = false
) {
    fun changeExpand() {
        isExpand = !isExpand
    }
}