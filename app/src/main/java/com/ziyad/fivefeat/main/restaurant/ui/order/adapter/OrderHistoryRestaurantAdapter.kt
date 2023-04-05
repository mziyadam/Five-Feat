package com.ziyad.fivefeat.main.restaurant.ui.order.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.fivefeat.R
import com.ziyad.fivefeat.databinding.ItemOrderHistoryRestaurantBinding
import com.ziyad.fivefeat.model.Order
import com.ziyad.fivefeat.utils.convertMillisToString
import com.ziyad.fivefeat.utils.toCurrencyFormat

class OrderHistoryRestaurantAdapter(private val items: ArrayList<Order>, private val onCLicked:(Order)->(Unit)) :
    RecyclerView.Adapter<OrderHistoryRestaurantAdapter.OrderHistoryRestaurantViewHolder>() {
    class OrderHistoryRestaurantViewHolder(val binding: ItemOrderHistoryRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderHistoryRestaurantViewHolder(
            ItemOrderHistoryRestaurantBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: OrderHistoryRestaurantViewHolder, position: Int) {
        val order = items[position]
        holder.apply {
            itemView.setOnClickListener {
                onCLicked(order)
            }
            binding.apply {
                tvOrderName.text = order.userName
                tvOrderDate.text = convertMillisToString(order.time)
                tvOrderStatus.text=order.state
                when(order.state){
                    "QUEUE"->{
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(itemView.context.resources.getColor(R.color.cyan))
                    }
                    "READY"->{
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(itemView.context.resources.getColor(R.color.green))
                    }
                    else->{
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(itemView.context.resources.getColor(R.color.cream))
                    }
                }
                var count = 0
                for (i in order.orderList) {
                    count += (i.price * i.count)
                }
                tvOrderPrice.text = count.toString().toCurrencyFormat()
            }

        }
    }

    override fun getItemCount() = items.size
}