package com.ziyad.fivefeat.main.user.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.fivefeat.databinding.ItemOrderBinding
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.utils.loadImage
import com.ziyad.fivefeat.utils.toCurrencyFormat
import java.util.*

class OrderAdapter(private val items: ArrayList<Menu>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val menu = items[position]
        holder.binding.apply {
            ivMenuPhoto.loadImage(menu.photoUrl)
            tvMenuName.text = menu.name
            tvMenuPrice.text = (menu.price*menu.count).toString().toCurrencyFormat()
            tvMenuPriceSingle.text=menu.price.toString().toCurrencyFormat()
            tvMenuCount.text = "${menu.count} pcs"

        }
    }

    override fun getItemCount() = items.size
}
