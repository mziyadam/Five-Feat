package com.ziyad.fivefeat.auth.restaurant

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ziyad.fivefeat.databinding.ActivityRegisterRestaurantBinding
import com.ziyad.fivefeat.main.restaurant.HomeRestaurantActivity
import com.ziyad.fivefeat.model.Restaurant
import com.ziyad.fivefeat.utils.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RegisterRestaurantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterRestaurantBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var photoId: String
    private var imageUri: Uri? = null
    private var imageUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        // Initialize Firebase Auth
        auth = Firebase.auth
        database = FirebaseFirestore.getInstance()
        photoId = UUID.randomUUID().toString()
        binding.apply {
            tvToRegister.setOnClickListener {
                startActivity(
                    Intent(
                        this@RegisterRestaurantActivity,
                        LoginRestaurantActivity::class.java
                    )
                )
                finish()
            }
            btnAddPhoto.setOnClickListener {
                val gallery = Intent()
                gallery.type = "image/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                gallery.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(gallery, 101)
            }
            btnRegister.setOnClickListener {
                if (etPassword.text.toString() == etReenterPassword.text.toString()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        auth.createUserWithEmailAndPassword(
                            etEmail.text.toString(),
                            etPassword.text.toString()
                        )
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                                    val user = auth.currentUser
                                    val restaurantName = etName.text.toString()
                                    val restaurant = Restaurant(
                                        user!!.uid, restaurantName, imageUrl.toString(),
                                        arrayListOf()
                                    )
                                    val db = Firebase.firestore
                                    db.collection("restaurants")
                                        .add(restaurant)
                                        .addOnSuccessListener {
                                            user.updateProfile(
                                                userProfileChangeRequest {
                                                    displayName = restaurantName
                                                    photoUri = Uri.parse(imageUrl.toString())
                                                }
                                            ).addOnCompleteListener {
                                                startActivity(
                                                    Intent(
                                                        this@RegisterRestaurantActivity,
                                                        HomeRestaurantActivity::class.java
                                                    )
                                                )
                                                finishAffinity()
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                this@RegisterRestaurantActivity,
                                                "Add note failed!",
                                                Toast.LENGTH_SHORT
                                            )
                                            Log.e("NOTES", "$e")
                                        }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(
                                        ContentValues.TAG,
                                        "createUserWithEmail:failure",
                                        task.exception
                                    )
                                    Toast.makeText(
                                        baseContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(
                        baseContext, "Re-enter password doesn't match.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            imageUri = data?.data
            Log.d("GET PICTURE", "Gallery picture : $imageUri")
            storage = FirebaseStorage.getInstance()
            storageReference = storage.getReference("restaurants/ $photoId")
            val uploadTask = storageReference.putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                storageReference.downloadUrl.addOnCompleteListener {
                    imageUrl = it.result
                    Log.d("UPLOAD PICTURE", "Upload picture: $imageUrl")
                    binding.ivAddPhoto.loadImage(imageUrl.toString())
                }
            }

        }

    }

}