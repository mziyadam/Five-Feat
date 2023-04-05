package com.ziyad.fivefeat.main.restaurant.ui.order.main

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantPageViewModel : ViewModel() {
    private val _orders = MutableLiveData<ArrayList<Order>>()
    val orders: LiveData<ArrayList<Order>> = _orders
    private val _index = MutableLiveData<String>()

    init {
        _orders.value = arrayListOf()
    }

    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setState(state: String) {
        _index.value = state
    }

    fun loadData() {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("orders")
                .whereEqualTo("restaurantId", user?.uid.toString())
                .get()
                .addOnSuccessListener { result ->
                    var tempList = arrayListOf<Order>()
                    tempList = _orders.value!!
                    for (document in result) {
                        val menuList = arrayListOf<Menu>()
                        for (i in document.get("orderList") as ArrayList<*>) {
                            val temp = i as HashMap<*, *>
                            val menu = Menu(
                                temp["id"].toString(),
                                temp["name"].toString(),
                                temp["photoUrl"].toString(),
                                temp["price"].toString().toInt(),
                                temp["count"].toString().toInt()
                            )
                            menuList.add(menu)
                            Log.e("TEZ", menu.toString())
                        }
                        val mOrder = Order(
                            document.getString("id").toString(),
                            document.getString("restaurantId").toString(),
                            document.getString("restaurantName").toString(),
                            document.getString("restaurantPhotoUrl").toString(),
                            document.getString("userId").toString(),
                            menuList,
                            document.getLong("time")!!,
                            document.getString("state").toString(),
                            document.getString("userName").toString(),
                        )
                        tempList.add(mOrder)
                    }
                    _orders.value = tempList
                }
                .addOnFailureListener { exception ->
                    Log.w("TEZ", "Error getting documents.", exception)
                }
        }
    }

    fun clearList() {
        _orders.value?.clear()
    }
}