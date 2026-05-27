package com.example.calendartracker.data

class SettingsRepository(
    private val dataStore: SettingsDataStore
) {

    val isCalendarView = dataStore.isCalendarView

    suspend fun toggleCalendarView(current: Boolean) {
        dataStore.setCalendarView(!current)
    }
}