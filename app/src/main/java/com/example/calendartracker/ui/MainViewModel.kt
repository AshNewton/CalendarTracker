package com.example.calendartracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerType
import com.example.calendartracker.data.TrackerValue
import com.example.calendartracker.repository.TrackerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repo: TrackerRepository) : ViewModel() {

    val trackers = repo.trackers.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val entries = repo.entries.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addTracker(
        name: String,
        type: TrackerType,
        min: Int?,
        max: Int?,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            val result = repo.addTracker(name, type, min, max)
            onResult(result)
        }
    }

    fun deleteTracker(tracker: TrackerDefinition) {

        viewModelScope.launch {
            repo.deleteTracker(tracker)
        }
    }

    fun saveEntryForDate(
        time: Long,
        values: List<TrackerValue>
    ) {
        viewModelScope.launch {
            repo.saveEntryForDate(
                time,
                values
            )
        }
    }

    fun getValuesForEntry(entryId: Int, onResult: (List<TrackerValue>) -> Unit) {
        viewModelScope.launch {
            val values = repo.getValuesForEntry(entryId)
            onResult(values)
        }
    }

    fun loadValuesForDay(
        time: Long,
        onLoaded: (List<TrackerValue>) -> Unit
    ) {
        viewModelScope.launch {
            val values = repo.getValuesForDay(time)
            onLoaded(values)
        }
    }
}