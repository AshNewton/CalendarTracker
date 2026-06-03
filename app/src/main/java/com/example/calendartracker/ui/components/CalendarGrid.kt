package com.example.calendartracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerEntry
import com.example.calendartracker.data.TrackerValue
import com.example.calendartracker.util.buildMonthGrid
import com.example.calendartracker.util.buildTrackerValueMap
import com.example.calendartracker.util.dayKey
import com.example.calendartracker.util.getHeatColor
import com.example.calendartracker.util.isToday
import java.time.YearMonth

@Composable
fun CalendarGrid(
    month: YearMonth,
    entries: List<TrackerEntry>,
    onDayClick: (Long) -> Unit,
    selectedTrackerId: Int?,
    trackers: List<TrackerDefinition>,
    values: List<TrackerValue>
) {
    val days = remember(month) {
        buildMonthGrid(month)
    }

    val entryKeys = remember(entries) {
        entries.map { it.dayKey }.toSet()
    }

    val valueMap = remember(values, entries, selectedTrackerId) {
        buildTrackerValueMap(
            values = values,
            entries = entries,
            trackerId = selectedTrackerId
        )
    }

    val tracker = trackers.firstOrNull { it.id == selectedTrackerId }

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

                    val dayKey = day?.let {
                        dayKey(month.year, month.monthValue, it)
                    }

                    val hasEntry = dayKey != null && entryKeys.contains(dayKey)
                    val value = valueMap[dayKey]

                    val heatColor = getHeatColor(tracker, value)

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clickable(enabled = hasEntry) {
                                if (day != null && dayKey != null) {
                                    onDayClick(dayKey)
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = heatColor
                        ),
                        border = if (isToday(dayKey))
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        else null
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = day?.toString() ?: "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (hasEntry) {
                                    Spacer(Modifier.height(3.dp))

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