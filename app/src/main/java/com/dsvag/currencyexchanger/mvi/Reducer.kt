package com.dsvag.currencyexchanger.mvi

typealias Reducer<State, Effect> = (state: State, effect: Effect) -> State