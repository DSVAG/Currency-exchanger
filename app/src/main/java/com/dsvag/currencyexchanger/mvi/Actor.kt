package com.dsvag.currencyexchanger.mvi

import io.reactivex.rxjava3.core.Observable

typealias Actor<State, Action, Effect> = (state: State, action: Action) -> Observable<Effect>