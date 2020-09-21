package com.dsvag.currencyexchanger.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.adapters.CoinAdapter
import com.dsvag.currencyexchanger.data.di.getAppComponent
import com.dsvag.currencyexchanger.databinding.ActivityCoinBinding
import com.dsvag.currencyexchanger.mvi.Feature
import com.dsvag.currencyexchanger.mvi.State
import com.dsvag.currencyexchanger.mvi.Store
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

    private val store
            by lazy { Store(State(), Feature.ActorImpl(getAppComponent().coinRepository), Feature.ReducerImpl()) }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerview()
        initSearchBar()
        initSwipeRefresh()

        store.observeState().subscribe(::render).addTo(disposable)

        store.accept(Feature.Action.Init)
        store.accept(Feature.Action.FetchCoins)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }


    private fun render(state: State) {
        binding.swipeRefresh.isRefreshing = state.isLoading

        adapter.setData(state.filteredCoins)

        if (state.error.isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(state.error)
                .setPositiveButton("Sumbit") { dialog, _ ->
                    dialog.dismiss()
                    store.accept(Feature.Action.ClearError)
                }
                .show()
        }
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { store.accept(Feature.Action.FetchCoins) }
    }

    private fun initSearchBar() {
        binding.searchBar.textChanges()
            .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe({ store.accept(Feature.Action.FilterCoins(it.toString())) }, {}, {})
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

    companion object {
        private val TAG = CoinActivity::class.simpleName
    }
}