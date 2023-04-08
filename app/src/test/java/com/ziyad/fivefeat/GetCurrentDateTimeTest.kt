package com.ziyad.fivefeat

import com.ziyad.fivefeat.utils.getCurrentDateTime
import org.junit.Assert
import org.junit.Test

class GetCurrentDateTimeTest {
    @Test
    fun getCurrentDateTime_GetDateInMillisAndNotNull() {
        val currentDateTime = getCurrentDateTime()
        Assert.assertTrue(currentDateTime is Long && currentDateTime != null)
    }
}