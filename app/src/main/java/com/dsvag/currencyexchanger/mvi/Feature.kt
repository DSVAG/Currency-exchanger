package com.dsvag.currencyexchanger.mvi

import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.repositorys.CoinRepository
import io.reactivex.rxjava3.core.Observable

class Feature {

    sealed class Effect {
        object StartedLoading : Effect()
        data class LoadedList(val list: List<Coin>) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class Action {
        object LoadNewList : Action()
    }

    class ActorImpl(private val repository: CoinRepository) : Actor<State, Action, Effect> {

        override fun invoke(state: State, action: Action): Observable<Effect> = when (action) {
            is Action.LoadNewList -> {
                //TODO
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {

        override fun invoke(state: State, effect: Effect): State = when (effect) {
            Effect.StartedLoading -> state.copy(isLoading = true)

            is Effect.LoadedList -> state.copy(isLoading = false, list = effect.list)

            is Effect.ErrorLoading -> state.copy(isLoading = false, error = effect.throwable.toString())
        }
    }
}