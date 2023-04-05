package com.ziyad.fivefeat.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ziyad.fivefeat.auth.restaurant.LoginRestaurantActivity
import com.ziyad.fivefeat.auth.restaurant.RegisterRestaurantActivity
import com.ziyad.fivefeat.auth.user.LoginActivity
import com.ziyad.fivefeat.auth.user.RegisterActivity
import com.ziyad.fivefeat.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.apply {
            btnToLogin.setOnClickListener {
                startActivity(Intent(this@LandingActivity, LoginActivity::class.java))
            }
            btnToRegister.setOnClickListener {
                startActivity(Intent(this@LandingActivity, RegisterActivity::class.java))
            }
            btnLoginRestaurant.setOnClickListener {
                startActivity(Intent(this@LandingActivity, LoginRestaurantActivity::class.java))
            }
            btnRegisterRestaurant.setOnClickListener {
                startActivity(Intent(this@LandingActivity, RegisterRestaurantActivity::class.java))
            }
        }
    }
}