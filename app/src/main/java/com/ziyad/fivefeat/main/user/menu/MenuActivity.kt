package com.ziyad.fivefeat.main.user.menu

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.databinding.ActivityMenuBinding
import com.ziyad.fivefeat.main.user.menu.adapter.MenuAdapter
import com.ziyad.fivefeat.main.user.order.OrderActivity
import com.ziyad.fivefeat.main.user.ui.home.HomeFragment.Companion.SCANNED_ID
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Order
import com.ziyad.fivefeat.notification.NotificationWorker
import com.ziyad.fivefeat.repository.Repository
import com.ziyad.fivefeat.utils.getCurrentDateTime
import com.ziyad.fivefeat.utils.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val menuViewModel: MenuViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val restaurantId = intent.getStringExtra(SCANNED_ID)
        Log.e("TEZ", restaurantId.toString())
        menuViewModel.getRestaurant(restaurantId!!)
        menuViewModel.restaurant.observe(this) { restaurant ->
            if (restaurant != null) {
                binding.apply {
                    tvRestaurantName.text = restaurant.name
                    ivRestaurantPhoto.loadImage(restaurant.photoUrl)
                    rvMenu.apply {
                        adapter = MenuAdapter(
                            restaurant.menuList
                        ) { menu, count ->
                            menuViewModel.changeAmountOrder(menu, count)
                        }
                        setHasFixedSize(true)
                    }
                }
                menuViewModel.orderList.observe(this) { menus ->
                    val mMenus = arrayListOf<Menu>()
                    for (i in menus.keys) {
                        if (menus[i]!! > 0)
                            mMenus.add(
                                i.copy(count = menus[i]!!)
                            )
                    }
                    val mOrder = Order(
                        UUID.randomUUID().toString(),
                        restaurant.id,
                        restaurant.name,
                        restaurant.photoUrl,
                        Repository.getInstance().getUser()?.uid.toString(),
                        mMenus,
                        getCurrentDateTime(),
                        "QUEUE",
                        menuViewModel.user?.displayName.toString()
                    )
                    binding.btnCart.setOnClickListener {
                        val db = Firebase.firestore
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.collection("orders")
                                .add(mOrder)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                    val workManager = WorkManager.getInstance(applicationContext)
                                    val dataBuilder = Data.Builder()
                                    dataBuilder.apply {
                                        putString("ORDER_ID", mOrder.id)
                                    }
                                    val inputData = dataBuilder.build()
                                    val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                                        .setInputData(inputData)
                                        .build()
                                    workManager.enqueue(workRequest)
                                    startActivity(
                                        Intent(
                                            this@MenuActivity,
                                            OrderActivity::class.java
                                        ).putExtra("ORDER", mOrder)
                                    )
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        }
                    }
                }
            }
        }
    }
}