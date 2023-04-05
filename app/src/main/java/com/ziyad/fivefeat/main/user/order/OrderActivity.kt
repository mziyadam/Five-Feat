package com.ziyad.fivefeat.main.user.order

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ziyad.fivefeat.R
import com.ziyad.fivefeat.databinding.ActivityOrderBinding
import com.ziyad.fivefeat.main.user.order.adapter.OrderAdapter
import com.ziyad.fivefeat.model.Order
import com.ziyad.fivefeat.utils.convertMillisToString
import com.ziyad.fivefeat.utils.toCurrencyFormat

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private val orderViewModel: OrderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val mOrder = intent.getParcelableExtra<Order>("ORDER")
        if (mOrder != null) {
            orderViewModel.getQueueBefore(mOrder)
            orderViewModel.getOrderUpdate(mOrder)
        }
        orderViewModel.order.observe(this) { order ->
            binding.apply {
                rvOrder.apply {
                    adapter = order?.let {
                        OrderAdapter(
                            it.orderList
                        )
                    }
                    setHasFixedSize(true)
                }
                when (order?.state) {
                    "QUEUE" -> {
//                        tvQueueMessage.text = "Still in queue"
                        orderViewModel.getQueueBefore(order)
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.cyan))
                        orderViewModel.queueBefore.observe(this@OrderActivity) {
                            binding.tvQueueMessage.text = "In store queue: $it people"
                        }
                    }
                    "READY" -> {
                        tvQueueMessage.text = "Your order is ready! Please come collect it"
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.green))
//                        showNotification(order)
                    }
                    else -> {
                        tvQueueMessage.text = "This order has been completed"
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.cream))
                    }
                }
                var total = 0
                if (order != null) {
                    tvOrderTime.text = convertMillisToString(order.time)
                    for (i in order.orderList) {
                        total += (i.count * i.price)
                    }
                    tvTotal.text = total.toString().toCurrencyFormat()
                    tvOrderNameDetail.text = order.restaurantName
                    tvOrderStatus.text = order.state
                }
                btnBack.setOnClickListener {
                    finish()
                }
            }
        }
    }
}