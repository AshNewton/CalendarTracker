package com.example.calendartracker.util

import java.util.Calendar

fun dayRange(time: Long): Pair<Long, Long> {
    val cal = Calendar.getInstance().apply {
        timeInMillis = time
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val start = cal.timeInMillis
    cal.add(Calendar.DAY_OF_YEAR, 1)
    val end = cal.timeInMillis

    return start to end
}
