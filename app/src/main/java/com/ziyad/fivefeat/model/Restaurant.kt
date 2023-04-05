package com.ziyad.fivefeat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    val id:String,
    val name: String,
    val photoUrl:String,
    val menuList: ArrayList<Menu>
): Parcelable
