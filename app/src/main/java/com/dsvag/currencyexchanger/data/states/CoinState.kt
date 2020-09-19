package com.dsvag.currencyexchanger.data.states

import com.dsvag.currencyexchanger.data.models.latest.Coin

sealed class CoinState {
    object LoadingState : CoinState()

    data class DataState(val list: List<Coin>) : CoinState()

    data class ErrorState(val error: String) : CoinState()
}