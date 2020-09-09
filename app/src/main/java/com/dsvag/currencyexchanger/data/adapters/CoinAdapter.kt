package com.dsvag.currencyexchanger.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.databinding.RowCoinBinding

class CoinAdapter() : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var data: MutableList<Coin> = ArrayList()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return CoinViewHolder(
            RowCoinBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                moveToTop(position)
            }
        }
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

    fun moveToTop(position: Int) {
        data[0] = data.removeAt(    position)
        notifyItemMoved(position, 0)
        recyclerView.scrollToPosition(0)
    }

    inner class CoinViewHolder(private val itemBinding: RowCoinBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(coin: Coin) {
            itemBinding.name.text = coin.name
            itemBinding.symbol.text = coin.symbol
            itemBinding.price.hint = coin.quote.usd.priceForOne.toString()
            itemBinding.lastUpdate.text =
                coin.quote.usd.lastUpdated.split("T")[1].replace(".000Z", "")

            if (coin.quote.usd.priceForOne != coin.quote.usd.priceInAnotherCoin) {
                itemBinding.price.text!!.clear()
            } else {
                itemBinding.price.setText(coin.quote.usd.priceInAnotherCoin.toString())
            }

            itemBinding.price.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    moveToTop(adapterPosition)
                }
            }
        }
    }
}