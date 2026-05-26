package com.example.calendartracker.repository

import com.example.calendartracker.data.*
import com.example.calendartracker.util.dayKey

class TrackerRepository(private val dao: TrackerDao) {

    val trackers = dao.getTrackers()
    val entries = dao.getEntries()

    suspend fun addTracker(name: String, type: String, min: Int?, max: Int?): Result<Unit> {

        if (name.isBlank()) {
            return Result.failure(Exception("Name is required"))
        }

        val exists = dao.countTrackersByName(name) > 0
        if (exists) {
            return Result.failure(Exception("Tracker name must be unique"))
        }

        dao.insertTracker(
            TrackerDefinition(
                name = name.trim(),
                type = type,
                minValue = min,
                maxValue = max
            )
        )

        return Result.success(Unit)
    }

    suspend fun getValuesForEntry(entryId: Int): List<TrackerValue> {
        return dao.getValuesForEntry(entryId)
    }

    suspend fun saveEntryForDate(
        time: Long,
        values: List<TrackerValue>
    ) {

        val key = dayKey(time)

        // check if day already exists
        val existingEntry = dao.getEntryByDay(key)

        val entryId = existingEntry?.id
            ?: dao.insertEntry(
                TrackerEntry(
                    date = time,
                    dayKey = key
                )
            ).toInt()

        // remove old values for this day
        dao.deleteValuesForEntry(entryId)

        // insert updated values
        values.forEach {
            dao.insertValue(
                it.copy(entryId = entryId)
            )
        }
    }

    suspend fun getValuesForDay(time: Long): List<TrackerValue> {
        return dao.getValuesForDay(
            dayKey(time)
        )
    }
}