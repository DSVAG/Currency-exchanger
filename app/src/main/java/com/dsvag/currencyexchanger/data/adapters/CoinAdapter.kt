package com.dsvag.currencyexchanger.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.databinding.RowCoinBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinAdapter : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var data: MutableList<Coin> = ArrayList()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return CoinViewHolder(
            RowCoinBinding.inflate(inflater, parent, false),
            moveToTop = {
                data.add(0, data.removeAt(it))
                notifyItemMoved(it, 0)
                recyclerView.scrollToPosition(0)
            },
            {
                data[0].quote.usd.priceInAnotherCoin = it

                data.forEachIndexed { index, coin ->
                    if (index != 0) {
                        coin.reprice(data[0].quote.usd.price * it)
                        notifyItemChanged(index)
                    }
                }
            }
        )
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    fun setData(data: List<Coin>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class CoinViewHolder(
        private val itemBinding: RowCoinBinding,
        private val moveToTop: (position: Int) -> Unit,
        private val reprice: (price: Double) -> Unit,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(coin: Coin) {
            itemBinding.name.text = coin.name
            itemBinding.symbol.text = coin.symbol
            itemBinding.price.hint = coin.quote.usd.price.toString()
            itemBinding.lastUpdate.text =
                coin.quote.usd.lastUpdated.split("T")[1].replace(".000Z", "")

            if (coin.quote.usd.priceInAnotherCoin > 0) {
                itemBinding.price.setText(coin.quote.usd.priceInAnotherCoin.toString())
            } else {
                itemBinding.price.text?.clear()
            }

            itemBinding.price.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    itemBinding.price.text?.clear()

                    moveToTop(adapterPosition)

                    itemBinding.price.textChanges()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribe({ reprice("0".plus(it).toDouble()) }, {}, {})
                }
            }
        }
    }
}