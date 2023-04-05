package com.ziyad.fivefeat.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Repository {
    fun getUser(): FirebaseUser? = Firebase.auth.currentUser

    fun logout() {
        Firebase.auth.signOut()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(): Repository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    instance = Repository()
                }
                return instance as Repository
            }
        }
    }
}