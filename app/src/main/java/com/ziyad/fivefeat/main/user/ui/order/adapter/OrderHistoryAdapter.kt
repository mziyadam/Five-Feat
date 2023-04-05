package com.ziyad.fivefeat.main.user.ui.order.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ziyad.fivefeat.R
import com.ziyad.fivefeat.databinding.ItemOrderHistoryBinding
import com.ziyad.fivefeat.model.Order
import com.ziyad.fivefeat.utils.convertMillisToString
import com.ziyad.fivefeat.utils.loadImage
import com.ziyad.fivefeat.utils.toCurrencyFormat
import java.util.*


class OrderHistoryAdapter(private val items: ArrayList<Order>, private val onCLicked:(Order)->(Unit)) :
    RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
    class OrderHistoryViewHolder(val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderHistoryViewHolder(ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = items[position]
        holder.apply {
            itemView.setOnClickListener {
                onCLicked(order)
            }
            binding.apply {
                ivMenuPhoto.loadImage(order.restaurantPhotoUrl)
                tvOrderName.text = order.restaurantName
                tvOrderDate.text = convertMillisToString(order.time)
                tvOrderStatus.text=order.state
                var count = 0
                for (i in order.orderList) {
                    count += (i.price * i.count)
                }
                tvOrderPrice.text = count.toString().toCurrencyFormat()
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
            }

        }
    }

    override fun getItemCount() = items.size
}