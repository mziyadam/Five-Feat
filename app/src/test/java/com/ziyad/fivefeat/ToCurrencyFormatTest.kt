package com.ziyad.fivefeat

import com.ziyad.fivefeat.utils.toCurrencyFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class ToCurrencyFormatTest {
    @Test
    fun toCurrencyFormat_FormatRawPrice() {
        val rawPrice = "10000"
        val expectedCurrencyFormatPrice = "Rp10.000"
        assertEquals(rawPrice.toCurrencyFormat(), expectedCurrencyFormatPrice)
    }
}