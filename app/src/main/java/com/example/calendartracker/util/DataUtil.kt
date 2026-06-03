package com.example.calendartracker.util

import androidx.compose.runtime.remember
import com.example.calendartracker.data.TrackerEntry
import com.example.calendartracker.data.TrackerValue

fun buildTrackerValueMap(
    values: List<TrackerValue>,
    entries: List<TrackerEntry>,
    trackerId: Int?
): Map<Long, TrackerValue> {

    if (trackerId == null) {
        return emptyMap()
    }

    val entryById = entries.associateBy { it.id }

    return values
        .filter { it.trackerId == trackerId }
        .mapNotNull { value ->
            val entry = entryById[value.entryId]
                ?: return@mapNotNull null

            entry.dayKey to value
        }
        .toMap()
}