package com.dsvag.currencyexchanger.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.R
import com.dsvag.currencyexchanger.data.models.latest.Coin
import kotlinx.android.synthetic.main.row_coin.view.*


class CoinAdapter() : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var data: MutableList<Coin> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CoinViewHolder(
            RowCoinBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int = data.size

    fun setData(data: ArrayList<Coin>) {
        this.data = data
        notifyDataSetChanged()
    }

    class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(coin: Coin) {
            itemView.name.text = coin.name
            itemView.symbol.text = coin.symbol
            itemView.price.setText("${coin.quote.usd.price}")
            itemView.volume24h.text = "${coin.quote.usd.volume24h}"
            itemView.percentChange1h.text = "${coin.quote.usd.percentChange1h}"
            itemView.percentChange24h.text = "${coin.quote.usd.percentChange24h}"
            itemView.percentChange7d.text = "${coin.quote.usd.percentChange7d}"
            itemView.lastUpdate.text = coin.quote.usd.lastUpdated

            itemView.setOnClickListener {
                when (itemView.expand.visibility) {
                    View.VISIBLE -> {
                        itemView.expand.visibility = View.GONE
                    }
                    View.GONE -> {
                        itemView.expand.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
