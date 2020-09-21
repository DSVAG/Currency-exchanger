package com.dsvag.currencyexchanger.data.models.requestCoins

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("USD")
    val usd: Usd,
)