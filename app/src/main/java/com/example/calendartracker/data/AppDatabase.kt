package com.example.calendartracker.data

import android.content.Context
import androidx.room.*

@Database(
    entities = [TrackerDefinition::class, TrackerEntry::class, TrackerValue::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): TrackerDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tracker_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}