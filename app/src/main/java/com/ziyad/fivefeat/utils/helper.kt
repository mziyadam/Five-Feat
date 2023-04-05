package com.ziyad.fivefeat.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .into(this)
}

fun String.toCurrencyFormat(): String {
    val localeID = Locale("in", "ID")
    val doubleValue = this.toDoubleOrNull() ?: return this
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    numberFormat.minimumFractionDigits = 0
    return numberFormat.format(doubleValue)
}

fun getCurrentDateTime(): Long {
    return Calendar.getInstance().timeInMillis
}

fun convertMillisToString(timeMillis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeMillis
    val sdf = SimpleDateFormat("dd MMM yyyy HH:mm z", Locale.getDefault())
    return sdf.format(calendar.time)
}

fun sortMillisToDateDescending(dateList:ArrayList<Long>):ArrayList<String>{
    val result = dateList.sortedByDescending {
        it
    }
    val sortedDateList= arrayListOf<String>()
    for(i in result){
        sortedDateList.add(convertMillisToString(i))
    }
    return sortedDateList
}