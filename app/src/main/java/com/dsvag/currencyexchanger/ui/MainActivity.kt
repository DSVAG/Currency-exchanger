package com.dsvag.currencyexchanger.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.adapters.CoinAdapter
import com.dsvag.currencyexchanger.data.utils.getAppComponent
import com.dsvag.currencyexchanger.databinding.ActivityMainBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding
            by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    private val repository by lazy { getAppComponent().coinRepository }

    private val keyBoardUtils by lazy { getAppComponent().keyBoardUtils }

    private val adapter by lazy { CoinAdapter(keyBoardUtils) }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerview()
        initSearchBar()

        apiCall()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun apiCall() {
        repository.getCoins()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ adapter.setData(it) }, { })
            .addTo(disposable)
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
        private val TAG = MainActivity::class.simpleName
    }
}