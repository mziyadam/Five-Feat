package com.ziyad.fivefeat.main.restaurant.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziyad.fivefeat.model.Order

class OrderRestaurantViewModel : ViewModel() {
    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> = _order
    private val _state=MutableLiveData<String>()
    val state:LiveData<String> =_state
    init {
        _state.value="NONE"
    }
    fun setOrder(mOrder: Order) {
        _order.value = mOrder
    }
    fun setState(newState:String){
        _state.value=newState
    }
}