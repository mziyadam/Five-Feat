package com.ziyad.fivefeat.main.restaurant.order.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ziyad.fivefeat.R
import com.ziyad.fivefeat.model.Menu
import com.ziyad.fivefeat.model.Order


class StateDialogFragment : DialogFragment() {
    private lateinit var listener: StateDialogListener

    interface StateDialogListener {
        fun onSelect(newState: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as StateDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Set order status")
                .setItems(
                    R.array.state_array
                ) { dialog, which ->
                    // The 'which' argument contains the index position
                    // of the selected item
                    Log.e("TEZ", which.toString())
                    var newState = "NONE"
                    when (which) {
                        0 -> newState = "QUEUE"
                        1 -> newState = "READY"
                        2 -> newState = "DONE"
                    }
                    val orderId = arguments?.getString("orderId")
                    val database = Firebase.firestore
                    database.collection("orders")
                        .whereEqualTo("id", orderId)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val orderList = arrayListOf<Menu>()
                                for (i in document.get("orderList") as ArrayList<*>) {
                                    val temp = i as HashMap<*, *>
                                    val menu = Menu(
                                        temp["id"].toString(),
                                        temp["name"].toString(),
                                        temp["photoUrl"].toString(),
                                        temp["price"].toString().toInt(),
                                        temp["count"].toString().toInt()
                                    )
                                    orderList.add(menu)
                                    Log.e("TEZ", menu.toString())
                                }
                                val newOrder = Order(
                                    document.getString("id").toString(),
                                    document.getString("restaurantId").toString(),
                                    document.getString("restaurantName").toString(),
                                    document.getString("restaurantPhotoUrl").toString(),
                                    document.getString("userId").toString(),
                                    orderList,
                                    document.getLong("time")!!,
                                    newState,
                                    document.getString("userName").toString(),
                                )
                                database.collection("orders")
                                    .document(document.id)
                                    .set(newOrder)
                                    .addOnSuccessListener {
                                        Log.w("TEZ", "added $it")
                                        listener.onSelect(newState)
                                    }.addOnFailureListener { exception ->
                                        Log.w("TEZ", "Error getting documents.", exception)
                                    }
                            }
                        }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}