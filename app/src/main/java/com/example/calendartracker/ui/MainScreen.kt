package com.example.dailytracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.calendartracker.data.*
import com.example.calendartracker.ui.AddTrackerScreen
import com.example.calendartracker.ui.CalendarScreen
import com.example.calendartracker.ui.DayDetailScreen
import com.example.calendartracker.ui.EditDayScreen
import com.example.calendartracker.ui.MainViewModel
import com.example.calendartracker.ui.Screen
import java.util.*

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val trackers by viewModel.trackers.collectAsState()
    val entries by viewModel.entries.collectAsState()

    var screen by remember { mutableStateOf(Screen.CALENDAR) }
    var selectedEntry by remember { mutableStateOf<TrackerEntry?>(null) }

    when (screen) {

        Screen.CALENDAR -> CalendarScreen(
            entries = entries,
            onAddEdit = { screen = Screen.EDIT_DAY },
            onAddTracker = { screen = Screen.ADD_TRACKER },
            onSelectEntry = {
                selectedEntry = it
                screen = Screen.DAY_DETAIL
            }
        )

        Screen.DAY_DETAIL -> DayDetailScreen(
            entry = selectedEntry,
            trackers = trackers,
            viewModel = viewModel,
            onBack = { screen = Screen.CALENDAR },
            onEdit = { screen = Screen.EDIT_DAY }
        )

        Screen.EDIT_DAY -> EditDayScreen(
            trackers = trackers,
            viewModel = viewModel,
            onCancel = { screen = Screen.CALENDAR },
            onSave = {
                screen = Screen.CALENDAR
            }
        )

        Screen.ADD_TRACKER -> AddTrackerScreen(
            trackers = trackers,
            viewModel = viewModel,
            onCancel = { screen = Screen.CALENDAR },
            onDone = { screen = Screen.CALENDAR }
        )
    }
}
