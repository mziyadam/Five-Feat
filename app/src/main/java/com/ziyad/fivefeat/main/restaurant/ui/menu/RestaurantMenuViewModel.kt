package com.ziyad.fivefeat.main.restaurant.ui.menu

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Restaurant
import com.ziyad.fivefeat.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RestaurantMenuViewModel : ViewModel() {

    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant
    private val _restaurantDocumentId = MutableLiveData<String>()

    init {
        getRestaurant()
    }

    fun deleteMenu(mMenu: Menu) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        runBlocking {
            db.collection("restaurants")
                .whereEqualTo("id", user?.uid)
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
                        menuList.remove(mMenu)
                        val newRestaurant = Restaurant(
                            document.getString("id").toString(),
                            document.getString("name").toString(),
                            document.getString("photoUrl").toString(),
                            menuList
                        )
                        db.collection("restaurants")
                            .document(document.id)
                            .set(newRestaurant)
                            .addOnSuccessListener {
                                Log.w("TEZ", "deleted $it")
                                getRestaurant()
                            }.addOnFailureListener { exception ->
                                Log.w("TEZ", "Error getting documents.", exception)
                            }
                    }
                }
        }
    }

    @SuppressLint("NewApi")
    fun getRestaurant() {
        val restaurantId: String = Repository.getInstance().getUser()?.uid.toString()
        val db = Firebase.firestore
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("restaurants")
                .whereEqualTo("id", restaurantId)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        _restaurantDocumentId.value = document.id
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