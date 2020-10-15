package com.dsvag.currencyexchanger.mvi

import io.reactivex.rxjava3.core.Flowable

typealias Actor<State, Action, Effect> = (state: State, action: Action) -> Flowable<Effect>