package com.ziyad.fivefeat.main.user.menu

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Restaurant
import com.ziyad.fivefeat.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {
    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant
    private val _orderList = MutableLiveData<HashMap<Menu, Int>>()
    val orderList: LiveData<HashMap<Menu, Int>> = _orderList
    val user = Repository.getInstance().getUser()
    private var orderMap = hashMapOf<Menu, Int>()

    init {
        _orderList.value = hashMapOf()
    }

    fun changeAmountOrder(menu: Menu, count: Int) {
        orderMap[menu] = count
        _orderList.postValue(orderMap)
    }

    //    fun getRestaurant(restaurantId:String)=Repository.getInstance().getRestaurant(restaurantId)
    @SuppressLint("NewApi")
    fun getRestaurant(restaurantId: String) {
        val db = Firebase.firestore
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("restaurants")
                .whereEqualTo("id", restaurantId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val menuList = arrayListOf<Menu>()
                        for (i in document.get("menuList") as ArrayList<*>) {
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
                        _restaurant.value = Restaurant(
                            document.getString("id").toString(),
                            document.getString("name").toString(),
                            document.getString("photoUrl").toString(),
                            menuList
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TEZ", "Error getting documents.", exception)
                }
        }
    }
}