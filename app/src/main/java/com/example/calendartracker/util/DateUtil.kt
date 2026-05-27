package com.example.calendartracker.util

import java.time.YearMonth
import java.util.Calendar

fun dayKey(timeMillis: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timeMillis

    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.timeInMillis
}

fun buildMonthGrid(month: YearMonth): List<Int?> {

    val firstDay = month.atDay(1)

    // Sunday-based offset (0–6)
    val offset = (firstDay.dayOfWeek.value % 7)

    val daysInMonth = month.lengthOfMonth()

    val result = mutableListOf<Int?>()

    repeat(offset) {
        result.add(null)
    }

    for (day in 1..daysInMonth) {
        result.add(day)
    }

    while (result.size % 7 != 0) {
        result.add(null)
    }

    return result
}
