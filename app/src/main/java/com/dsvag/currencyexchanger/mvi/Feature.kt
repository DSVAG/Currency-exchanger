package com.dsvag.currencyexchanger.mvi

import com.dsvag.currencyexchanger.data.models.dbCoins.Coin
import com.dsvag.currencyexchanger.data.repositorys.CoinRepository
import io.reactivex.rxjava3.core.Flowable

class Feature {
    sealed class Effect {
        object StartLoading : Effect()
        object FetchSuccess : Effect()
        object ClearError : Effect()

        data class FilterCoins(val filter: String) : Effect()
        data class CoinsChanged(val coins: List<Coin>) : Effect()
        data class FetchError(val throwable: Throwable) : Effect()
    }

    sealed class Action {
        object Init : Action()
        object FetchCoins : Action()
        object ClearError : Action()

        data class FilterCoins(val filter: String) : Action()
    }

    class ActorImpl(private val repository: CoinRepository) : Actor<State, Action, Effect> {

        override fun invoke(state: State, action: Action): Flowable<Effect> {
            return when (action) {

                Action.FetchCoins -> {
                    repository.getCoins()
                        .map<Effect> { Effect.FetchSuccess }
                        .onErrorReturn { Effect.FetchError(it) }
                        .toFlowable()
                }

                Action.Init -> repository.observeCoins().map { Effect.CoinsChanged(it) }

                Action.ClearError -> Flowable.just(Effect.ClearError)

                is Action.FilterCoins -> Flowable.just(Effect.FilterCoins(action.filter))

            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {

        override fun invoke(state: State, effect: Effect): State {
            return when (effect) {
                Effect.StartLoading -> state.copy(isLoading = true)

                Effect.FetchSuccess -> state.copy(isLoading = false)

                Effect.ClearError -> state.copy(error = "")

                is Effect.CoinsChanged -> state.copy(
                    coins = effect.coins,
                    filteredCoins = filterOut(effect.coins, state.filterBy)
                )

                is Effect.FetchError -> state.copy(
                    isLoading = false,
                    error = effect.throwable.localizedMessage.toString()
                )

                is Effect.FilterCoins -> state.copy(
                    filteredCoins = filterOut(state.coins, effect.filter),
                    filterBy = effect.filter
                )
            }
        }

        private fun filterOut(coins: List<Coin>, filter: String): List<Coin> {
            return coins.filter { coin ->
                coin.slug.toLowerCase().contains(filter) || coin.symbol.toLowerCase().contains(filter)
            }
        }
    }
}