package com.ziyad.fivefeat

import com.ziyad.fivefeat.utils.convertMillisToString
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class ConvertMillisToStringTest {
    @Test
    fun convertMillisToString_Convert(){
        val currentDateTime= Calendar.getInstance()
        val currentDateTimeInMillis= currentDateTime.timeInMillis

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentDateTimeInMillis
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm z", Locale.getDefault())
        val expectedDate=sdf.format(calendar.time)
        Assert.assertEquals(convertMillisToString(currentDateTimeInMillis), expectedDate)
    }
}