package com.github.ursteiner.todofx.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {

    private val YEAR_MONTH_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    fun getYearMonth(beforeXMonths: Int): String {
        return LocalDate.now().minusMonths(beforeXMonths.toLong()).format(YEAR_MONTH_FORMATTER)
    }

}