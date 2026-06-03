package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.calendartracker.data.*

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val trackers by viewModel.trackers.collectAsState()
    val entries by viewModel.entries.collectAsState()

    var screen by remember { mutableStateOf(Screen.CALENDAR) }
    var selectedEntry by remember { mutableStateOf<TrackerEntry?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ){
        when (screen) {

        Screen.CALENDAR -> CalendarScreen(
            viewModel = viewModel,
            entries = entries,
            trackers = trackers,
            onAddEdit = { screen = Screen.EDIT_DAY },
            onManageTrackers = { screen = Screen.MANAGE_TRACKERS },
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
            entry = selectedEntry,
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
            onCancel = { screen = Screen.MANAGE_TRACKERS },
            onDone = { screen = Screen.MANAGE_TRACKERS }
        )

        Screen.MANAGE_TRACKERS -> {
            ManageTrackersScreen(
                trackers = trackers,
                viewModel = viewModel,
                onAddTracker = {
                    screen = Screen.ADD_TRACKER
                },
                onBack = {
                    screen = Screen.CALENDAR
                }
            )
        }
    }}


}
