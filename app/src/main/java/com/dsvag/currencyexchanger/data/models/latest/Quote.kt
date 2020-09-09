package com.dsvag.currencyexchanger.data.models.latest

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("USD")
    val usd: Usd
)