package com.example.calendartracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDao {

    // =====================================================
    // Trackers
    // =====================================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracker(tracker: TrackerDefinition)

    @Query("SELECT * FROM trackers")
    fun getTrackers(): Flow<List<TrackerDefinition>>

    @Query("SELECT COUNT(*) FROM trackers WHERE LOWER(name) = LOWER(:name)")
    suspend fun countTrackersByName(name: String): Int

    @Delete
    suspend fun deleteTracker(tracker: TrackerDefinition)


    // =====================================================
    // Entries
    // =====================================================

    @Insert
    suspend fun insertEntry(entry: TrackerEntry): Long

    @Query("SELECT * FROM entries ORDER BY date DESC")
    fun getEntries(): Flow<List<TrackerEntry>>

    @Query("SELECT * FROM entries WHERE date BETWEEN :start AND :end LIMIT 1")
    suspend fun getEntryForDay(start: Long, end: Long): TrackerEntry?

    @Query("SELECT * FROM entries WHERE dayKey = :dayKey LIMIT 1")
    suspend fun getEntryByDay(dayKey: Long): TrackerEntry?


    // =====================================================
    // Values
    // =====================================================

    @Insert
    suspend fun insertValue(value: TrackerValue)

    @Query("SELECT * FROM tracker_values WHERE entryId = :entryId")
    suspend fun getValuesForEntry(entryId: Int): List<TrackerValue>

    @Query("DELETE FROM tracker_values WHERE entryId = :entryId")
    suspend fun deleteValuesForEntry(entryId: Int)

    @Transaction
    suspend fun getValuesForDay(dayKey: Long): List<TrackerValue> {

        val entry = getEntryByDay(dayKey)
            ?: return emptyList()

        return getValuesForEntry(entry.id)
    }

    @Query("""
    SELECT tv.* FROM tracker_values tv
    INNER JOIN entries e ON tv.entryId = e.id
    WHERE e.dayKey BETWEEN :startDayKey AND :endDayKey
    """)
    suspend fun getValuesForMonth(
        startDayKey: Long,
        endDayKey: Long
    ): List<TrackerValue>
}