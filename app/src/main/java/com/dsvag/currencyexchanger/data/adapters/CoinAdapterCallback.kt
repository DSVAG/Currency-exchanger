package com.dsvag.currencyexchanger.data.adapters

import java.math.BigDecimal

interface CoinAdapterCallback {
    fun moveToTop(position: Int)
    fun reprice(usd: BigDecimal)
}