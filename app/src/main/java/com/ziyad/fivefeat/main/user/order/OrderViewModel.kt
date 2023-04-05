package com.ziyad.fivefeat.main.user.order

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Order
import java.util.ArrayList
import java.util.HashMap

class OrderViewModel:ViewModel() {
    private val _order=MutableLiveData<Order>()
    val order:LiveData<Order> =_order
    private val _queueBefore=MutableLiveData<Int>()
    val queueBefore:LiveData<Int> =_queueBefore
    init {
        _queueBefore.value=0
    }
    fun getQueueBefore(mOrder: Order){
        val db = Firebase.firestore
        db.collection("orders")
            .whereLessThan("time", mOrder.time)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TEZ", "QUEUE BEFORE: ${document.data}")
                    db.collection("orders")
                        .document(document.id)
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e)
                                return@addSnapshotListener
                            }
                            if (snapshot != null && snapshot.exists()) {
                                Log.d("TEZ", "QUEUE BEFORE: ${snapshot.data}")
                                var mCounter=0
                                db.collection("orders")
                                    .whereLessThan("time", mOrder.time)
                                    .get()
                                    .addOnSuccessListener { result ->
                                        for (doc in result) {
                                            Log.d("TEZ", "QUEUE BEFORE: ${document.data}")
                                            if (doc.getString("state").toString() == "QUEUE")
                                                mCounter++
                                        }
                                        _queueBefore.value=mCounter
                                    }
                            } else {
                                Log.d("TEZ", "Current data: null")
                            }
                        }
                }
            }
    }
    fun getOrderUpdate(mOrder: Order){
        val db = Firebase.firestore
        db.collection("orders")
            .whereEqualTo("id", mOrder.id)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("orders")
                        .document(document.id)
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e)
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
                                _order.value=newOrder
                            } else {
                                Log.d("TEZ", "Current data: null")
                            }
                        }
                }
            }
    }
}