package com.example.calendartracker.data

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromValueState(value: ValueState): String =
        value.name

    @TypeConverter
    fun toValueState(value: String): ValueState =
        ValueState.valueOf(value)

    @TypeConverter
    fun fromTrackerType(type: TrackerType): String =
        type.name

    @TypeConverter
    fun toTrackerType(value: String): TrackerType =
        TrackerType.valueOf(value)
}