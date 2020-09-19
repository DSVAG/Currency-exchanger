package com.dsvag.currencyexchanger.data.presenters

import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.states.CoinState
import com.dsvag.currencyexchanger.data.utils.getAppComponent
import com.dsvag.currencyexchanger.ui.CoinActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class CoinPresenter {

    private lateinit var view: CoinActivity

    private val repository by lazy { view.getAppComponent().coinRepository }

    private val disposable = CompositeDisposable()

    private var currentState: CoinState? = null

    fun bind(view: CoinActivity) {
        this.view = view
        dbSubscribe()
    }

    fun unBind() {

    }

    private fun reduce(
        previous: CoinState?,
        list: List<Coin> = emptyList(),
        errorMsg: String = "",
    ): CoinState {
        if (previous == null) {
            apiCall()
        }
        if (list.isNotEmpty()) {
            this.currentState = CoinState.DataState(list)
        }
        if (errorMsg.isNotEmpty()) {
            this.currentState = CoinState.ErrorState(errorMsg)
        }

        return this.currentState!!
    }

    private fun apiCall() {
        repository.getCoins()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(disposable)
    }

    private fun dbSubscribe() {
        repository.subToDb()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.render(reduce(currentState, it))
            }, {
            })
            .addTo(disposable)
    }
}