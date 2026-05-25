package com.example.calendartracker.repository

import com.example.calendartracker.data.*
import com.example.calendartracker.util.dayRange

class TrackerRepository(private val dao: TrackerDao) {

    val trackers = dao.getTrackers()
    val entries = dao.getEntries()

    suspend fun addTracker(
        name: String,
        type: String,
        min: Int?,
        max: Int?
    ) {
        dao.insertTracker(
            TrackerDefinition(
                name = name,
                type = type,
                minValue = min,
                maxValue = max
            )
        )
    }

    suspend fun addEntry(date: Long, values: List<TrackerValue>) {

        val entryId = dao.insertEntry(
            TrackerEntry(date = date)
        )

        values.forEach {
            dao.insertValue(
                it.copy(entryId = entryId.toInt())
            )
        }
    }

    suspend fun getOrCreateEntryForDay(time: Long): TrackerEntry {
        val (start, end) = dayRange(time)

        val existing = dao.getEntryForDay(start, end)
        if (existing != null) return existing

        val id = dao.insertEntry(TrackerEntry(date = start))
        return TrackerEntry(id = id.toInt(), date = start)
    }

    suspend fun getValuesForEntry(entryId: Int): List<TrackerValue> {
        return dao.getValuesForEntry(entryId)
    }
}