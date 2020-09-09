package com.dsvag.currencyexchanger.data.models.latest

import com.google.gson.annotations.SerializedName

data class Latest(
    val status: Status,
    @SerializedName("data")
    val coins: List<Coin>
)