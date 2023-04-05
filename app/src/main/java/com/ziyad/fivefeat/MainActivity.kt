package com.ziyad.fivefeat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.auth.LandingActivity
import com.ziyad.fivefeat.main.restaurant.HomeRestaurantActivity
import com.ziyad.fivefeat.main.user.HomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth
        supportActionBar?.hide()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val db = Firebase.firestore
            val user = Firebase.auth.currentUser
            db.collection("restaurants")
                .whereEqualTo("id", user?.uid.toString())
                .get()
                .addOnSuccessListener { result ->
                    var temp = "NULL"
                    for (document in result) {
                        temp = document.getString("id").toString()
                    }
                    if(temp!="NULL"){
                        startActivity(Intent(this, HomeRestaurantActivity::class.java))
                        finishAffinity()
                    }else{
                        startActivity(Intent(this, HomeActivity::class.java))
                        finishAffinity()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TEZ", "Error getting documents.", exception)
                    startActivity(Intent(this, LandingActivity::class.java))
                    finishAffinity()
                }
        }else{
            startActivity(Intent(this, LandingActivity::class.java))
            finishAffinity()
        }
    }
}