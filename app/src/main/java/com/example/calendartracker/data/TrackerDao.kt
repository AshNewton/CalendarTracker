package com.example.calendartracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDao {

    @Insert suspend fun insertTracker(tracker: TrackerDefinition)
    @Query("SELECT * FROM trackers")
    fun getTrackers(): Flow<List<TrackerDefinition>>

    @Insert suspend fun insertEntry(entry: TrackerEntry): Long
    @Query("SELECT * FROM entries ORDER BY date DESC")
    fun getEntries(): Flow<List<TrackerEntry>>

    @Insert suspend fun insertValue(value: TrackerValue)

    @Query("SELECT * FROM tracker_values WHERE entryId = :entryId")
    suspend fun getValuesForEntry(entryId: Int): List<TrackerValue>

    @Query("SELECT * FROM entries WHERE date BETWEEN :start AND :end LIMIT 1")
    suspend fun getEntryForDay(start: Long, end: Long): TrackerEntry?


}