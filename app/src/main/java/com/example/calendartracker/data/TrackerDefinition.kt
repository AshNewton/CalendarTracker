package com.example.calendartracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trackers")
data class TrackerDefinition(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String // "number", "bool", "text"
)