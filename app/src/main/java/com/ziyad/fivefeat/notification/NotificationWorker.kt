package com.ziyad.fivefeat.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.R
import com.ziyad.fivefeat.main.user.order.OrderActivity
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Order
import com.ziyad.fivefeat.utils.convertMillisToString
import java.util.ArrayList
import java.util.HashMap

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params){

    private fun getPendingIntent(order: Order): PendingIntent? {
        val intent = Intent(applicationContext, OrderActivity::class.java).apply {
            putExtra("ORDER", order)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
    private fun showNotification(order: Order){
        val pendingIntent = getPendingIntent(order)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "channel_name",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "description"
            }
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(applicationContext, "channel_id")
            .setSmallIcon(R.drawable.ic_baseline_shopping_cart_24)
            .setContentTitle(order.restaurantName)
            .setContentText(
                "Your order at ${convertMillisToString(order.time)} is ready"
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, builder.build())
        }
    }

    override fun doWork(): Result {
        val db = Firebase.firestore
        val orderId = inputData.getString("ORDER_ID")
        db.collection("orders")
            .whereEqualTo("id", orderId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("orders")
                        .document(document.id)
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.w(ContentValues.TAG, "Listen failed.", e)
                                return@addSnapshotListener
                            }
                            if (snapshot != null && snapshot.exists()) {
                                Log.d("TEZ", "Current data: ${snapshot.data}")
                                val orderList = arrayListOf<Menu>()
                                for (i in snapshot.get("orderList") as ArrayList<*>) {
                                    val temp = i as HashMap<*, *>
                                    val menu = Menu(
                                        temp["id"].toString(),
                                        temp["name"].toString(),
                                        temp["photoUrl"].toString(),
                                        temp["price"].toString().toInt(),
                                        temp["count"].toString().toInt()
                                    )
                                    orderList.add(menu)
                                    Log.e("TEZ", menu.toString())
                                }
                                val newOrder = Order(
                                    snapshot.getString("id").toString(),
                                    snapshot.getString("restaurantId").toString(),
                                    snapshot.getString("restaurantName").toString(),
                                    snapshot.getString("restaurantPhotoUrl").toString(),
                                    snapshot.getString("userId").toString(),
                                    orderList,
                                    snapshot.getLong("time")!!,
                                    snapshot.getString("state").toString(),
                                    snapshot.getString("userName").toString(),
                                )
                                if(newOrder.state=="READY") {
                                    showNotification(newOrder)
                                }
                            } else {
                                Log.d("TEZ", "Current data: null")
                            }
                        }
                }
            }
        return Result.success()
    }
}