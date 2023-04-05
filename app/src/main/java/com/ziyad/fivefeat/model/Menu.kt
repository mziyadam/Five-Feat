package com.ziyad.fivefeat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
    val id:String,
    val name:String,
    val photoUrl:String,
    val price:Int,
    var count:Int=0
):Parcelable
