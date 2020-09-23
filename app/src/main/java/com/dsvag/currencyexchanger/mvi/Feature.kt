package com.dsvag.currencyexchanger.mvi

import androidx.recyclerview.widget.DiffUtil
import com.dsvag.currencyexchanger.data.models.dbCoins.Coin
import com.dsvag.currencyexchanger.data.repositorys.CoinRepository
import com.dsvag.currencyexchanger.data.utils.CoinDiffUtilCallback
import io.reactivex.rxjava3.core.Flowable
import java.math.BigDecimal

class Feature {
    sealed class Effect {
        object StartLoading : Effect()
        object FetchSuccess : Effect()
        object ClearError : Effect()

        data class FilterCoins(val filter: String) : Effect()
        data class CoinsChanged(val coins: List<Coin>) : Effect()
        data class FetchError(val throwable: Throwable) : Effect()
        data class MoveToTop(val position: Int) : Effect()
        data class Reprice(val usd: BigDecimal) : Effect()
    }

    sealed class Action {
        object Init : Action()
        object FetchCoins : Action()
        object ClearError : Action()

        data class FilterCoins(val filter: String) : Action()
        data class MoveToTop(val position: Int) : Action()
        data class Reprice(val usd: BigDecimal) : Action()
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

                Action.Init -> {
                    repository.observeCoins()
                        .map<Effect> { Effect.CoinsChanged(it) }
                        .onErrorReturn { Effect.FetchError(it) }
                }

                Action.ClearError -> Flowable.just(Effect.ClearError)

                is Action.FilterCoins -> Flowable.just(Effect.FilterCoins(action.filter))

                is Action.MoveToTop -> Flowable.just(Effect.MoveToTop(action.position))

                is Action.Reprice -> Flowable.just(Effect.Reprice(action.usd))
            }
        }
    }

    class ReducerImpl : Reducer<State, Effect> {

        override fun invoke(state: State, effect: Effect): State {
            return when (effect) {
                Effect.StartLoading -> state.copy(isLoading = true)

                Effect.FetchSuccess -> state.copy(isLoading = false)

                Effect.ClearError -> state.copy(error = "")

                is Effect.CoinsChanged -> {
                    val filteredCoins = filterOut(effect.coins, state.filterBy)

                    state.copy(
                        coins = effect.coins,
                        filteredCoins = filteredCoins,
                        diffResult = DiffUtil.calculateDiff(
                            CoinDiffUtilCallback(state.filteredCoins, filteredCoins),
                        )
                    )
                }

                is Effect.FetchError -> state.copy(
                    isLoading = false,
                    error = effect.throwable.localizedMessage.toString()
                )

                is Effect.FilterCoins -> {
                    val filteredCoins = filterOut(state.coins, effect.filter)

                    state.copy(
                        filteredCoins = filteredCoins,
                        filterBy = effect.filter,
                        diffResult = DiffUtil.calculateDiff(CoinDiffUtilCallback(state.filteredCoins, filteredCoins))
                    )
                }

                is Effect.MoveToTop -> {
                    val movedCoins = moveToTop(state.filteredCoins, effect.position)

                    state.copy(
                        filteredCoins = movedCoins,
                        diffResult = DiffUtil.calculateDiff(CoinDiffUtilCallback(state.filteredCoins, movedCoins))
                    )
                }

                is Effect.Reprice -> {
                    val repricedCoins = reprice(state.filteredCoins, effect.usd)

                    state.copy(
                        filteredCoins = repricedCoins,
                        diffResult = DiffUtil.calculateDiff(CoinDiffUtilCallback(state.filteredCoins, repricedCoins))
                    )
                }
            }
        }

        private fun moveToTop(coins: List<Coin>, position: Int): List<Coin> {
            val newCoins = coins.toMutableList()
            newCoins.add(0, newCoins.removeAt(position))
            return newCoins
        }

        private fun reprice(coins: List<Coin>, usd: BigDecimal): List<Coin> {
            val supportCoin = coins.first()

            return coins.map { coin ->
                coin.copy(priceInAnotherCoin = usd * supportCoin.price / coin.price)
            }
        }

        private fun filterOut(coins: List<Coin>, filter: String): List<Coin> {
            return coins.filter { coin ->
                coin.slug.toLowerCase().contains(filter) || coin.symbol.toLowerCase().contains(filter)
            }
        }
    }
}