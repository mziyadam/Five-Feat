package com.ziyad.fivefeat.main.restaurant.order

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ziyad.fivefeat.R
import com.ziyad.fivefeat.databinding.ActivityOrderRestaurantBinding
import com.ziyad.fivefeat.main.restaurant.order.dialog.StateDialogFragment
import com.ziyad.fivefeat.main.user.order.adapter.OrderAdapter
import com.ziyad.fivefeat.model.Order
import com.ziyad.fivefeat.utils.convertMillisToString
import com.ziyad.fivefeat.utils.toCurrencyFormat

class OrderRestaurantActivity : AppCompatActivity(), StateDialogFragment.StateDialogListener {
    private lateinit var binding: ActivityOrderRestaurantBinding
    private val orderRestaurantViewModel: OrderRestaurantViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val mOrder = intent.getParcelableExtra<Order>("ORDER")
        if (mOrder != null) {
            orderRestaurantViewModel.setOrder(mOrder)
            orderRestaurantViewModel.setState(mOrder.state)
        }
        orderRestaurantViewModel.order.observe(this) { order ->
            binding.apply {
                rvOrder.apply {
                    adapter = order?.let {
                        OrderAdapter(
                            it.orderList
                        )
                    }
                    setHasFixedSize(true)
                }
                var total = 0
                if (order != null) {
                    tvOrderTime.text = convertMillisToString(order.time)
                    for (i in order.orderList) {
                        total += (i.count * i.price)
                    }
                    tvTotal.text = total.toString().toCurrencyFormat()
                    tvOrderNameDetail.text = order.userName
                    tvOrderStatus.text = order.state
                }
                tvOrderStatus.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("orderId", order.id)
                    val dialog = StateDialogFragment()
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "StateDialogFragment")
                }
                btnBack.setOnClickListener {
                    finish()
                }
            }
        }
        orderRestaurantViewModel.state.observe(this) {
            binding.apply {
                tvOrderStatus.text = it
                when (it) {
                    "QUEUE" -> {
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.cyan))
                    }
                    "READY" -> {
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.green))
                    }
                    else -> {
                        tvOrderStatus.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.cream))
                    }
                }
            }
        }
    }

    override fun onSelect(newState: String) {
        orderRestaurantViewModel.setState(newState)
    }
}