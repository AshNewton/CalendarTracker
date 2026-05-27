package com.example.calendartracker.repository

import com.example.calendartracker.data.*
import com.example.calendartracker.util.dayKey


class TrackerRepository(
    private val dao: TrackerDao
) {

    val trackers = dao.getTrackers()
    val entries = dao.getEntries()


    // =====================================================
    // Trackers
    // =====================================================

    suspend fun addTracker(
        name: String,
        type: TrackerType,
        min: Int?,
        max: Int?
    ): Result<Unit> {

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

    suspend fun deleteTracker(tracker: TrackerDefinition) {
        dao.deleteTracker(tracker)
    }

    // =====================================================
    // Entries
    // =====================================================

    suspend fun saveEntryForDate(
        time: Long,
        values: List<TrackerValue>
    ) {

        val key = dayKey(time)

        val entryId = dao.getEntryByDay(key)?.id
            ?: dao.insertEntry(
                TrackerEntry(
                    date = time,
                    dayKey = key
                )
            ).toInt()

        dao.deleteValuesForEntry(entryId)

        values.forEach { value ->
            dao.insertValue(value.copy(entryId = entryId))
        }
    }

    // =====================================================
    // Values
    // =====================================================

    suspend fun getValuesForEntry(entryId: Int): List<TrackerValue> {
        return dao.getValuesForEntry(entryId)
    }

    suspend fun getValuesForDay(time: Long): List<TrackerValue> {
        return dao.getValuesForDay(dayKey(time))
    }
}