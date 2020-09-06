package com.dsvag.currencyexchanger.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.adapters.CoinAdapter
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.models.latest.Latest
import com.dsvag.currencyexchanger.data.network.ApiCoinData
import com.dsvag.currencyexchanger.data.until.OnItemClickListener
import com.dsvag.currencyexchanger.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val binding
            by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    private val adapter = CoinAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerview()
        initAdapter()

        apiCall()
    }

    private fun initRecyclerview() {
        binding.recyclerview.setHasFixedSize(false)
        binding.recyclerview.layoutManager = LinearLayoutManager(binding.recyclerview.context)
        binding.recyclerview.adapter = adapter

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun initAdapter() {
//        adapter.attachCallback(object : OnItemClickListener {
//            override fun onClick(position: Int) {
//            }
//        })
    }

    private fun apiCall() {
        getSingle().subscribe(
            {
                adapter.setData(it.coins as ArrayList<Coin>)
                Log.e(TAG, "${it.status.errorCode}")
            },
            {
                Log.e(TAG, "onError", it)
            }
        )
    }

    private fun getSingle(): Single<Latest> {
        return ApiCoinData
            .invoke()
            .create(ApiCoinData::class.java)
            .getCoins(200)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun hideKeyboard() {
        try {
            val inputMethodManager = getSystemService<InputMethodManager>()!!
            editTextInput.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            Log.e(TAG, "closeKeyboard: $e")
        }
    }

    companion object {
        private val TAG = MainActivity::class.simpleName
    }
}
