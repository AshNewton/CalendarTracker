package com.example.calendartracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class TrackerEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long
)