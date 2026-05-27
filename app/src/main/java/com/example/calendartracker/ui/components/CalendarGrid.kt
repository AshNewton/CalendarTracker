package com.example.calendartracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.TrackerEntry
import com.example.calendartracker.util.buildMonthGrid
import java.time.YearMonth
import java.util.*

@Composable
fun CalendarGrid(
    month: YearMonth,
    entries: List<TrackerEntry>,
    onDayClick: (Long) -> Unit
) {
    val days = remember(month) {
        buildMonthGrid(month)
    }

    val entryKeys = remember(entries) {
        entries.map { it.dayKey }.toSet()
    }

    fun toDayKey(year: Int, month: Int, day: Int): Long {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        days.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { day ->
                    val hasEntry = day != null && entryKeys.contains(
                        toDayKey(month.year, month.monthValue, day)
                    )

                    val isToday = run {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, 0)
                        cal.set(Calendar.MINUTE, 0)
                        cal.set(Calendar.SECOND, 0)
                        cal.set(Calendar.MILLISECOND, 0)

                        val todayKey = cal.timeInMillis

                        day != null && todayKey == toDayKey(
                            month.year,
                            month.monthValue,
                            day
                        )
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clickable(enabled = hasEntry) {
                                onDayClick(toDayKey(month.year, month.monthValue, day!!))
                            },
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (isToday)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                        ),
                        border = if (isToday)
                            BorderStroke(
                                2.dp,
                                MaterialTheme.colorScheme.primary
                            )
                        else null
                    ) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = day?.toString() ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color =
                                        if (isToday)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSurface
                                )

                                if (hasEntry) {
                                    Spacer(Modifier.height(4.dp))

                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}