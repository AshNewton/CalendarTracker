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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerEntry
import com.example.calendartracker.data.TrackerType
import com.example.calendartracker.data.TrackerValue
import com.example.calendartracker.util.buildMonthGrid
import java.time.YearMonth
import java.util.*

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

    val entryById = remember(entries) {
        entries.associateBy { it.id }
    }

    val valueMap: Map<Long, TrackerValue> = remember(values, selectedTrackerId, entries) {
        if (selectedTrackerId == null) return@remember emptyMap()

        values
            .filter { it.trackerId == selectedTrackerId }
            .mapNotNull { value ->
                val entry = entryById[value.entryId] ?: return@mapNotNull null
                entry.dayKey to value
            }
            .toMap()
    }

    val tracker = trackers.firstOrNull { it.id == selectedTrackerId }

    fun normalize(value: Float, min: Float, max: Float): Float {
        return ((value - min) / (max - min)).coerceIn(0f, 1f)
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

    @Composable
    fun getHeatColor(value: TrackerValue?): Color {
        if (value == null) {
            return MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
        }

        if (tracker == null) {
            return MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
        }

        return when (tracker.type) {
            TrackerType.BOOL -> {
                val bool = value.value.toBoolean()
                if (bool) {
                    Color.hsv(
                        hue = 120f,
                        saturation = 0.7f,
                        value = 0.85f
                    )
                } else {
                    Color.hsv(
                        hue = 0f,
                        saturation = 0.7f,
                        value = 0.85f
                    )
                }
            }

            TrackerType.NUMBER -> {
                val num = value.value?.toFloatOrNull()
                    ?: return Color.Gray

                val min = tracker.minValue?.toFloat() ?: 0f
                val max = tracker.maxValue?.toFloat() ?: 1f

                val normalized = normalize(num, min, max)

                val t =
                    if (tracker.higherIsBetter == false)
                        1f - normalized
                    else
                        normalized

                val hue = 120f * t

                Color.hsv(
                    hue = hue,
                    saturation = 0.7f,
                    value = 0.85f
                )
            }

            else -> MaterialTheme.colorScheme.surfaceVariant
        }
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

                    val dayKey = day?.let {
                        toDayKey(month.year, month.monthValue, it)
                    }

                    val hasEntry = dayKey != null && entryKeys.contains(dayKey)
                    val value = valueMap[dayKey]

                    val isToday = dayKey != null && run {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, 0)
                        cal.set(Calendar.MINUTE, 0)
                        cal.set(Calendar.SECOND, 0)
                        cal.set(Calendar.MILLISECOND, 0)
                        cal.timeInMillis == dayKey
                    }

                    val heatColor = getHeatColor(value)

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
                        border = if (isToday)
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