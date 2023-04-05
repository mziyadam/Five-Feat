@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused"
)

package com.ziyad.fivefeat.main.restaurant.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddMenuViewModel: ViewModel() {
    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var photoId: String = UUID.randomUUID().toString()
    private var imageUri: Uri? = null
    private var imageUrl: Uri? = null
}