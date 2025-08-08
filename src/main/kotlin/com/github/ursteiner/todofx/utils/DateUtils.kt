package com.github.ursteiner.todofx.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

object DateUtils {

    fun getYearMonth(beforeXMonths: Int): String{
        val c: Calendar = GregorianCalendar()
        c.setTime(Date())
        val sdf = SimpleDateFormat("yyyy-MM")
        c.add(Calendar.MONTH, -beforeXMonths)

        return sdf.format(c.getTime())
    }

}