package com.dsvag.currencyexchanger.mvi

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers

class Store<State, Action, Effect>(
    initialState: State,
    private val actor: Actor<State, Action, Effect>,
    private val reducer: Reducer<State, Effect>,
) {
    @Volatile
    var state = initialState
        private set

    private val actions = PublishProcessor.create<Action>()

    fun observeState(): Flowable<State> {
        return actions
            .observeOn(Schedulers.io())
            .flatMap { action -> actor(state, action) }
            .map { effect ->
                state = reducer(state, effect)
                state
            }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun accept(action: Action) {
        actions.onNext(action)
    }
}