package com.dsvag.currencyexchanger.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.adapters.CoinAdapter
import com.dsvag.currencyexchanger.data.presenters.CoinPresenter
import com.dsvag.currencyexchanger.data.states.CoinState
import com.dsvag.currencyexchanger.data.utils.getAppComponent
import com.dsvag.currencyexchanger.databinding.ActivityCoinBinding
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

    private val presenter = CoinPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerview()
        initSearchBar()
        initSwipeRefresh()

        presenter.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        presenter.unBind()
    }

    fun render(state: CoinState) {
        when (state) {
            is CoinState.LoadingState -> renderLoadingState()
            is CoinState.DataState -> renderDataState(state)
            is CoinState.ErrorState -> renderErrorState(state)
        }
    }

    private fun renderLoadingState() {
        binding.swipeRefresh.isRefreshing = true
    }

    private fun renderDataState(dataState: CoinState.DataState) {
        binding.swipeRefresh.isRefreshing = false
        adapter.setData(dataState.list)
    }

    private fun renderErrorState(errorState: CoinState.ErrorState) {
        binding.swipeRefresh.isRefreshing = false

        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(errorState.error)
            .show()
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
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

    companion object {
        private val TAG = CoinActivity::class.simpleName
    }
}