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

fun dayKey(year: Int, month: Int, day: Int): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1) // Calendar is 0-based
    cal.set(Calendar.DAY_OF_MONTH, day)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun localDateToMillis(date: java.time.LocalDate): Long {
    val cal = Calendar.getInstance()
    cal.set(date.year, date.monthValue - 1, date.dayOfMonth, 0, 0, 0)
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

fun isToday(dayKey: Long?): Boolean {
    if (dayKey == null) return false

    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.timeInMillis == dayKey
}
