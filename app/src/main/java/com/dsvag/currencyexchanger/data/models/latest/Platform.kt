package com.dsvag.currencyexchanger.data.models.latest

import com.google.gson.annotations.SerializedName

data class Platform(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("token_address")
    val tokenAddress: String
)