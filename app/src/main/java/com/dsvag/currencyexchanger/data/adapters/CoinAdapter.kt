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
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class CoinAdapter(
    private val keyBoardUtils: KeyBoardUtils,
    private val coinAdapterCallback: CoinAdapterCallback,
) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var data: MutableList<Coin> = ArrayList()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return CoinViewHolder(
            RowCoinBinding.inflate(inflater, parent, false),
            keyBoardUtils,
            coinAdapterCallback,
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
        data.clear()
        data.addAll(newData)

        diffResult.dispatchUpdatesTo(this)
    }

    class CoinViewHolder(
        private val itemBinding: RowCoinBinding,
        private val keyBoardUtils: KeyBoardUtils,
        private val coinAdapterCallback: CoinAdapterCallback,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private var disposable = CompositeDisposable()

        fun bind(coin: Coin) {
            itemBinding.name.text = coin.name
            itemBinding.symbol.text = coin.symbol
            itemBinding.price.hint = coin.price.setScale(5, 5).stripTrailingZeros().toString()
            itemBinding.lastUpdate.text = coin.lastUpdated

            if (coin.priceInAnotherCoin > BigDecimal.ZERO) {
                itemBinding.price.setText(coin.priceInAnotherCoin.setScale(0, 5).stripTrailingZeros().toString())
            } else {
                itemBinding.price.text?.clear()
            }

            itemBinding.root.setOnClickListener {
                itemBinding.price.requestFocus()
            }

            itemBinding.price.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    itemBinding.price.setSelection(itemBinding.price.text.toString().length)
                    keyBoardUtils.showKeyBoard(view)
                    coinAdapterCallback.moveToTop(adapterPosition)

                    itemBinding.price.textChanges()
                        .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .subscribe({
                            coinAdapterCallback.reprice((it.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO)
                                .setScale(5, 5)
                                .stripTrailingZeros()
                            )
                        }, {}, {})
                        .addTo(disposable)
                } else {
                    disposable.dispose()
                }
            }
        }
    }
}