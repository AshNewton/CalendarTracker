package com.example.calendartracker.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendartracker.data.SettingsRepository
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerType
import com.example.calendartracker.data.TrackerValue
import com.example.calendartracker.repository.TrackerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth

class MainViewModel(private val trackerRepo: TrackerRepository, private val settingsRepo: SettingsRepository) : ViewModel() {

    // =====================================================
    // Trackers
    // =====================================================

    val trackers = trackerRepo.trackers.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTracker(
        name: String,
        type: TrackerType,
        min: Int?,
        max: Int?,
        higherIsBetter: Boolean?,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            val result = trackerRepo.addTracker(name, type, min, max, higherIsBetter)
            onResult(result)
        }
    }

    fun deleteTracker(tracker: TrackerDefinition) {

        viewModelScope.launch {
            trackerRepo.deleteTracker(tracker)
        }
    }

    // =====================================================
    // Entries
    // =====================================================

    val entries = trackerRepo.entries.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun saveEntryForDate(
        time: Long,
        values: List<TrackerValue>
    ) {
        viewModelScope.launch {
            trackerRepo.saveEntryForDate(
                time,
                values
            )
        }
    }

    // =====================================================
    // Values
    // =====================================================

    private val _monthValues =
        MutableStateFlow<Map<YearMonth, List<TrackerValue>>>(emptyMap())
    val monthValues = _monthValues.asStateFlow()

    fun getValuesForEntry(entryId: Int, onResult: (List<TrackerValue>) -> Unit) {
        viewModelScope.launch {
            val values = trackerRepo.getValuesForEntry(entryId)
            onResult(values)
        }
    }

    fun loadValuesForDay(
        time: Long,
        onLoaded: (List<TrackerValue>) -> Unit
    ) {
        viewModelScope.launch {
            val values = trackerRepo.getValuesForDay(time)
            onLoaded(values)
        }
    }

    fun loadMonthValues(month: YearMonth) {
        viewModelScope.launch {
            val values = trackerRepo.getValuesForMonth(month)
            _monthValues.value += (month to values)
        }
    }

    // =====================================================
    // Settings
    // =====================================================

    val isCalendarView = settingsRepo.isCalendarView
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleCalendarView() {
        viewModelScope.launch {
            settingsRepo.toggleCalendarView(isCalendarView.value)
        }
    }
}