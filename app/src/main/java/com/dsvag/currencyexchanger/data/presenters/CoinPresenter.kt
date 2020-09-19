package com.dsvag.currencyexchanger.data.presenters

import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.states.CoinState
import com.dsvag.currencyexchanger.data.di.getAppComponent
import com.dsvag.currencyexchanger.ui.CoinActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class CoinPresenter {

    private lateinit var view: CoinActivity

    private val repository by lazy { view.getAppComponent().coinRepository }

    private val disposable = CompositeDisposable()

    private var currentState: CoinState = CoinState.NoDataState

    fun bind(view: CoinActivity) {
        this.view = view
        dbSubscribe()
    }

    fun unBind() {
        disposable.dispose()
    }

    private fun reduce(
        previous: CoinState,
        list: List<Coin> = emptyList(),
        errorMsg: String = "",
    ): CoinState {
        when {
            previous is CoinState.NoDataState -> {
                apiCall()
            }
            previous is CoinState.LoadingState -> {
                this.currentState = CoinState.LoadingState
            }
            list.isNotEmpty() -> {
                this.currentState = CoinState.DataState(list)
            }
            errorMsg.isNotEmpty() -> {
                this.currentState = CoinState.ErrorState(errorMsg)
            }
        }

        return this.currentState
    }

    private fun apiCall() {
        repository.getCoins()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { view.render(reduce(currentState, errorMsg = it.toString())) }
            .subscribe()
            .addTo(disposable)
    }

    private fun dbSubscribe() {
        repository.subToDb()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.render(CoinState.LoadingState)
            }
            .subscribe({
                view.render(reduce(currentState, list = it))
            }, {
                view.render(reduce(currentState, errorMsg = it.localizedMessage))
            })
            .addTo(disposable)
    }
}