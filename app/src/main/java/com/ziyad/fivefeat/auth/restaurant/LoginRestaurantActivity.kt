package com.ziyad.fivefeat.auth.restaurant

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.databinding.ActivityLoginRestaurantBinding
import com.ziyad.fivefeat.main.restaurant.HomeRestaurantActivity
import com.ziyad.fivefeat.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginRestaurantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRestaurantBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.apply {
            tvToRegister.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginRestaurantActivity,
                        RegisterRestaurantActivity::class.java
                    )
                )
                finish()
            }
            btnLogin.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
//                    Snackbar.make(it,"Loading",Snackbar.LENGTH_INDEFINITE).show()
                    auth.signInWithEmailAndPassword(
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(ContentValues.TAG, "signInWithEmail:success")
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
                                        if (temp != "NULL") {
                                            startActivity(
                                                Intent(
                                                    this@LoginRestaurantActivity,
                                                    HomeRestaurantActivity::class.java
                                                )
                                            )
                                            finishAffinity()
                                        } else {
                                            Repository.getInstance().logout()
                                            makeText(
                                                baseContext, "Restaurant not found.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w("TEZ", "Error getting documents.", exception)
                                    }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                                makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}