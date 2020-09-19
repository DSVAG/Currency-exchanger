package com.dsvag.currencyexchanger.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.adapters.CoinAdapter
import com.dsvag.currencyexchanger.data.di.getAppComponent
import com.dsvag.currencyexchanger.databinding.ActivityCoinBinding
import com.dsvag.currencyexchanger.mvi.State
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

class CoinActivity : AppCompatActivity() {

    private val binding
            by lazy(LazyThreadSafetyMode.NONE) { ActivityCoinBinding.inflate(layoutInflater) }

    private val keyBoardUtils by lazy { getAppComponent().keyBoardUtils }

    private val adapter by lazy { CoinAdapter(keyBoardUtils) }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerview()
        initSearchBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    fun render(state: State) {
        binding.swipeRefresh.isRefreshing = state.isLoading
        if (state.list.isNotEmpty()) {
            adapter.setData(state.list)
        }
        if (state.error.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(state.error)
                .show()
        }
    }

    private fun initSearchBar() {
        binding.searchBar.textChanges()
            .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({ adapter.filterOut(it.toString().toLowerCase()) }, {}, {})
            .addTo(disposable)
    }

    private fun initRecyclerview() {
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = LinearLayoutManager(binding.recyclerview.context)

        adapter.onAttachedToRecyclerView(binding.recyclerview)
        binding.recyclerview.adapter = adapter

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        keyBoardUtils.hideKeyBoard(binding.root)
                    }
                }
            }
        })
    }

    private fun apiCall() {
        repository.getCoins()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { }
            .subscribe()
            .addTo(disposable)
    }

    private fun dbSubscribe() {
        repository.subToDb()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(disposable)
    }

    companion object {
        private val TAG = CoinActivity::class.simpleName
    }
}