package com.ziyad.fivefeat.main.restaurant.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ziyad.fivefeat.repository.Repository

class RestaurantsSettingsViewModel : ViewModel() {
    private val _user = MutableLiveData<FirebaseUser>().apply {
        value = Repository.getInstance().getUser()
    }
    val name: LiveData<FirebaseUser> = _user

    fun logout() {
        Repository.getInstance().logout()
    }
}