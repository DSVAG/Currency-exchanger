package com.dsvag.currencyexchanger.mvi

import com.dsvag.currencyexchanger.data.models.dbCoins.Coin

data class State(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val filteredCoins: List<Coin> = emptyList(),
    val error: String = "",
    val filterBy: String = "",
)