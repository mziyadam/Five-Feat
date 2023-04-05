package com.ziyad.fivefeat.main.user.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ziyad.fivefeat.repository.Repository

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = Repository.getInstance().getUser()?.displayName
    }
    val text: LiveData<String> = _text
}