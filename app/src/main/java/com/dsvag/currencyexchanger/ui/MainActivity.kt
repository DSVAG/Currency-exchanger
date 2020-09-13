package com.dsvag.currencyexchanger.ui

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.adapters.CoinAdapter
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.repositorys.CoinRepository
import com.dsvag.currencyexchanger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding
            by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    private val repository by lazy { CoinRepository(application) }

    private val adapter = CoinAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerview()

        apiCall()
    }

    private fun apiCall() {
        val isDbEmpty = readFormDb().isEmpty()
        val data: MutableList<Coin> = ArrayList()

        repository.getCoins().subscribe({
            if (it.status.errorCode == 0) {
                data.addAll(it.coins)
            }
        }, {
            Log.e(TAG, "Error on api call", it)
        })

        if (isDbEmpty) {
            repository.addCoins(data)
        } else {
            repository.updateCoins(data)
        }

        adapter.setData(readFormDb())
    }

    private fun readFormDb(): List<Coin> {
        val list: MutableList<Coin> = ArrayList()

        repository.readCoins()?.subscribe({
            list.addAll(it)
        }, {
            Log.e(TAG, "Error on read DB", it)
        })

        return list
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