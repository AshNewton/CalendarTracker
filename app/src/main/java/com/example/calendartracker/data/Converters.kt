package com.example.calendartracker.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromValueState(value: ValueState): String = value.name

    @TypeConverter
    fun toValueState(value: String): ValueState = ValueState.valueOf(value)
}