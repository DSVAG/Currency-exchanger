package com.dsvag.currencyexchanger.data.models.requestCoins

import com.google.gson.annotations.SerializedName

data class Usd(
    @SerializedName("price")
    val price: Double,

    @SerializedName("volume_24h")
    val volume24h: Double,

    @SerializedName("percent_change_1h")
    val percentChange1h: Double,

    @SerializedName("percent_change_24h")
    val percentChange24h: Double,

    @SerializedName("percent_change_7d")
    val percentChange7d: Double,

    @SerializedName("market_cap")
    val marketCap: Double,

    @SerializedName("last_updated")
    val lastUpdated: String,
)