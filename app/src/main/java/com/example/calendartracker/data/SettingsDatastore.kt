package com.example.calendartracker.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    private val Context.dataStore by preferencesDataStore("settings")

    private val CALENDAR_VIEW_KEY =
        booleanPreferencesKey("calendar_view")

    val isCalendarView = context.dataStore.data
        .map { it[CALENDAR_VIEW_KEY] ?: false }

    suspend fun setCalendarView(value: Boolean) {
        context.dataStore.edit {
            it[CALENDAR_VIEW_KEY] = value
        }
    }
}