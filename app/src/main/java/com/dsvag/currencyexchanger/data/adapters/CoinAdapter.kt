package com.dsvag.currencyexchanger.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsvag.currencyexchanger.data.models.latest.Coin
import com.dsvag.currencyexchanger.data.untils.OnItemClickListener
import com.dsvag.currencyexchanger.databinding.RowCoinBinding

class CoinAdapter : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private var data: MutableList<Coin> = ArrayList()

    private var callback: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return CoinViewHolder(
            RowCoinBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(data[position])

        if (callback != null) {
            callback!!.onClick(position)
        }
    }

    override fun getItemCount(): Int = data.size

    fun attachCallback(callback: OnItemClickListener) {
        this.callback = callback
    }

    fun setData(data: List<Coin>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun changeExpand(position: Int) {
        data[position].changeExpand()
        notifyItemChanged(position)
    }

    class CoinViewHolder(private val itemBinding: RowCoinBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(coin: Coin) {
            itemBinding.name.text = coin.name
            itemBinding.symbol.text = coin.symbol
            itemBinding.price.setText("${coin.quote.usd.price}")
            itemBinding.volume24h.text = "${coin.quote.usd.volume24h}"
            itemBinding.percentChange1h.text = "${coin.quote.usd.percentChange1h}"
            itemBinding.percentChange24h.text = "${coin.quote.usd.percentChange24h}"
            itemBinding.percentChange7d.text = "${coin.quote.usd.percentChange7d}"
            itemBinding.lastUpdate.text = coin.quote.usd.lastUpdated

            if (coin.isExpand) {
                itemBinding.expand.visibility = View.VISIBLE
            } else {
                itemBinding.expand.visibility = View.GONE
            }
        }
    }
}