package com.github.ursteiner.todofx.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateUtilsTest {

    private lateinit var testCandidate: DateUtils

    @BeforeEach
    fun setUp(){
        testCandidate = DateUtils
    }

    @Test
    fun testGetYearMonth(){
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val currentDate = sdf.format(Date())

        assertEquals(currentDate, testCandidate.getYearMonth(0))
    }
}