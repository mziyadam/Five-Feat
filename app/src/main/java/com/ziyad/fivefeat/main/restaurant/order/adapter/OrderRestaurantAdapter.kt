package com.ziyad.fivefeat.main.restaurant.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.fivefeat.databinding.ItemOrderBinding
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.utils.loadImage
import com.ziyad.fivefeat.utils.toCurrencyFormat
import java.util.ArrayList

class OrderRestaurantAdapter(private val items: ArrayList<Menu>) :
    RecyclerView.Adapter<OrderRestaurantAdapter.OrderRestaurantViewHolder>() {
    class OrderRestaurantViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderRestaurantViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: OrderRestaurantViewHolder, position: Int) {
        val menu = items[position]
        holder.binding.apply {
            ivMenuPhoto.loadImage(menu.photoUrl)
            tvMenuName.text = menu.name
            tvMenuPrice.text = (menu.price*menu.count).toString().toCurrencyFormat()
            tvMenuCount.text = menu.count.toString()
        }
    }

    override fun getItemCount() = items.size
}
