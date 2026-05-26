package com.example.calendartracker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "entries",
    indices = [Index(value = ["dayKey"], unique = true)]
)
data class TrackerEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val dayKey: Long
)