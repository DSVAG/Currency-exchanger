package com.dsvag.currencyexchanger.ui

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.adapters.CoinAdapter
import com.dsvag.currencyexchanger.data.utils.getAppComponent
import com.dsvag.currencyexchanger.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class MainActivity : AppCompatActivity() {

    private val binding
            by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    private val repository by lazy { getAppComponent().coinRepository }

    private val adapter = CoinAdapter()

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerview()

        apiCall()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun apiCall() {
        repository.getCoins()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.setData(it)
            }, {

            })
            .addTo(disposable)
    }

    private fun initRecyclerview() {
        binding.mainContent.recyclerview.setHasFixedSize(true)
        binding.mainContent.recyclerview.layoutManager = LinearLayoutManager(binding.mainContent.recyclerview.context)

        adapter.onAttachedToRecyclerView(binding.mainContent.recyclerview)
        binding.mainContent.recyclerview.adapter = adapter

        binding.mainContent.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        hideKeyboard()
                    }
                }
            }
        })
    }

    private fun hideKeyboard() {
        try {
            val inputMethodManager = getSystemService<InputMethodManager>()!!
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            Log.e(TAG, "closeKeyboard: $e")
        }
    }

    companion object {
        private val TAG = MainActivity::class.simpleName
    }
}