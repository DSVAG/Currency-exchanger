package com.dsvag.currencyexchanger.mvi

import com.dsvag.currencyexchanger.data.models.latest.Coin

data class State(
    val isLoading: Boolean = false,
    val list: List<Coin> = emptyList(),
    val error: String = "",
)