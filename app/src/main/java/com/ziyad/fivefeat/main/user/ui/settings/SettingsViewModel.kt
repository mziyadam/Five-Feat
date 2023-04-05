package com.ziyad.fivefeat.main.user.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziyad.fivefeat.repository.Repository

class SettingsViewModel : ViewModel() {

    private val _name = MutableLiveData<String>().apply {
        value = Repository.getInstance().getUser()?.displayName.toString()
    }
    val name: LiveData<String> = _name

    fun logout(){
        Repository.getInstance().logout()
    }
}