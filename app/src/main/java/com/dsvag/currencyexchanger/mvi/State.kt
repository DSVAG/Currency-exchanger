package com.dsvag.currencyexchanger.mvi

import androidx.recyclerview.widget.DiffUtil
import com.dsvag.currencyexchanger.data.models.dbCoins.Coin
import com.dsvag.currencyexchanger.data.utils.CoinDiffUtilCallback

data class State(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val filteredCoins: List<Coin> = emptyList(),
    val error: String = "",
    val filterBy: String = "",
    val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(CoinDiffUtilCallback(coins, filteredCoins)),
)