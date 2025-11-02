package com.github.ursteiner.todofx.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DateUtilsTest {

    private lateinit var testCandidate: DateUtils
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    @BeforeEach
    fun setUp() {
        testCandidate = DateUtils
    }

    @Test
    fun testGetYearMonth() {
        val currentDate = dateTimeFormatter.format(LocalDate.now())

        assertEquals(currentDate, testCandidate.getYearMonth(0))
    }
}