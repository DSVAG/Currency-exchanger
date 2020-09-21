package com.dsvag.currencyexchanger.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.models.dbCoins.Coin
import com.dsvag.currencyexchanger.data.utils.KeyBoardUtils
import com.dsvag.currencyexchanger.databinding.RowCoinBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

class CoinAdapter(private val keyBoardUtils: KeyBoardUtils) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var data: MutableList<Coin> = ArrayList()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return CoinViewHolder(
            RowCoinBinding.inflate(inflater, parent, false),
            ::moveToTop,
            ::reprice,
            keyBoardUtils,
        )
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    fun setData(newData: List<Coin>, diffResult: DiffUtil.DiffResult) {
        diffResult.dispatchUpdatesTo(this)

        data.clear()
        data.addAll(newData)

        notifyDataSetChanged()
    }

    private fun moveToTop(position: Int) {
        data.add(0, this.data.removeAt(position))
        notifyItemMoved(position, 0)
        recyclerView.scrollToPosition(0)
    }

    private fun reprice(usd: Double) {
        val firstPrice = data.first().price
        data.first().reprice(usd)

        data.forEachIndexed { index, coin ->
            if (index != 0) {
                coin.reprice(firstPrice * usd)
                notifyItemChanged(index)
            }
        }
    }

    class CoinViewHolder(
        private val itemBinding: RowCoinBinding,
        private val moveToTop: (position: Int) -> Unit,
        private val reprice: (price: Double) -> Unit,
        private val keyBoardUtils: KeyBoardUtils,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private var disposable = CompositeDisposable()

        fun bind(coin: Coin) {
            itemBinding.name.text = coin.name
            itemBinding.symbol.text = coin.symbol
            itemBinding.price.hint = coin.price.toString()
            itemBinding.lastUpdate.text = coin.lastUpdated

            if (coin.priceInAnotherCoin > 0) {
                itemBinding.price.setText(coin.priceInAnotherCoin.toString())
            } else {
                itemBinding.price.text?.clear()
            }

            itemBinding.root.setOnClickListener {
                itemBinding.price.requestFocus()
            }

            itemBinding.price.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    itemBinding.price.text?.clear()
                    keyBoardUtils.showKeyBoard(view)

                    moveToTop(adapterPosition)

                    itemBinding.price.textChanges()
                        .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .subscribe({ reprice(it.toString().toDoubleOrNull() ?: 0.0) }, {}, {})
                        .addTo(disposable)

                } else {
                    disposable.dispose()
                }
            }
        }
    }
}