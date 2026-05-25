package com.example.calendartracker.data

import androidx.room.*

@Entity(
    tableName = "tracker_values",
    foreignKeys = [
        ForeignKey(
            entity = TrackerEntry::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrackerDefinition::class,
            parentColumns = ["id"],
            childColumns = ["trackerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TrackerValue(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entryId: Int,
    val trackerId: Int,
    val value: String? = null,
    val state: ValueState
)