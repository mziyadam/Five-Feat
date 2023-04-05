package com.ziyad.fivefeat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id:String,
    val restaurantId:String,
    val restaurantName:String,
    val restaurantPhotoUrl:String,
    val userId:String,
    val orderList: ArrayList<Menu>,
    val time:Long,
    val state:String,
    val userName:String
):Parcelable
