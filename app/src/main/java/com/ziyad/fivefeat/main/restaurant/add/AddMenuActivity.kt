package com.ziyad.fivefeat.main.restaurant.add

import android.app.Activity
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ziyad.fivefeat.databinding.ActivityAddMenuBinding
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Restaurant
import com.ziyad.fivefeat.utils.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMenuBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var photoId: String
    private var imageUri: Uri? = null
    private var imageUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Menu"
        auth = Firebase.auth
        database = FirebaseFirestore.getInstance()
        photoId = UUID.randomUUID().toString()

        binding.apply {
            btnAddPhoto.setOnClickListener {
                val gallery = Intent()
                gallery.type = "image/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                gallery.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(gallery, 101)
            }
            btnAddMenu.setOnClickListener {
                database = Firebase.firestore
                val user = Firebase.auth.currentUser
                lifecycleScope.launch(Dispatchers.IO) {
                    btnAddMenu.isClickable = false
                    database.collection("restaurants")
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
                                menuList.add(
                                    Menu(
                                        UUID.randomUUID().toString(),
                                        binding.etMenuName.text.toString(),
                                        imageUrl.toString(),
                                        binding.etMenuPrice.text.toString().toInt()
                                    )
                                )
                                val newRestaurant = Restaurant(
                                    document.getString("id").toString(),
                                    document.getString("name").toString(),
                                    document.getString("photoUrl").toString(),
                                    menuList
                                )

                                database.collection("restaurants")
                                    .document(document.id)
                                    .set(newRestaurant)
                                    .addOnSuccessListener {
                                        Log.w("TEZ", "added $it")
                                        finish()
                                    }.addOnFailureListener { exception ->
                                        Log.w("TEZ", "Error getting documents.", exception)
                                        Toast.makeText(
                                            this@AddMenuActivity,
                                            "Error adding menu.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddMenu.isClickable = true
                                    }
                            }
                        }
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