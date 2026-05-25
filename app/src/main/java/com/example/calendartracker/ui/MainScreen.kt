package com.example.dailytracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.ui.MainViewModel
import com.example.calendartracker.data.*
import com.example.calendartracker.ui.Screen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val trackers by viewModel.trackers.collectAsState()
    val entries by viewModel.entries.collectAsState()

    var screen by remember { mutableStateOf(Screen.CALENDAR) }
    var selectedEntry by remember { mutableStateOf<TrackerEntry?>(null) }

    when (screen) {
        // CALENDAR SCREEN
        // =====================================================
        Screen.CALENDAR -> {
            Column(Modifier.fillMaxSize().padding(16.dp)) {

                Text("Calendar", style = MaterialTheme.typography.headlineMedium)

                Spacer(Modifier.height(12.dp))

                Button(onClick = { screen = Screen.EDIT_DAY }) {
                    Text("Add / Edit Today")
                }

                Button(onClick = { screen = Screen.ADD_TRACKER }) {
                    Text("Add New Tracker")
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn {
                    items(entries) { entry ->

                        val dateStr = remember(entry.date) {
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(Date(entry.date))
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    selectedEntry = entry
                                    screen = Screen.DAY_DETAIL
                                }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(dateStr)
                                Text("Tap to view details")
                            }
                        }
                    }
                }
            }
        }
        // =====================================================
        // DAY DETAIL SCREEN
        // =====================================================
        Screen.DAY_DETAIL -> {

            val entry = selectedEntry

            Scaffold(
                bottomBar = {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding(), // ✅ fixes overlap
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Button(onClick = { screen = Screen.CALENDAR }) {
                            Text("Back")
                        }

                        Button(onClick = { screen = Screen.EDIT_DAY }) {
                            Text("Edit")
                        }
                    }
                }
            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {

                    if (entry == null) {
                        Text("No entry selected")
                        return@Column
                    }

                    Text(
                        "Day Details",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Date: ${Date(entry.date)}")

                    Spacer(Modifier.height(16.dp))

                    val valuesState = remember { mutableStateMapOf<Int, TrackerValue>() }

                    LaunchedEffect(entry.id) {
                        viewModel.getValuesForEntry(entry.id) { values ->
                            valuesState.clear()
                            values.forEach {
                                valuesState[it.trackerId] = it
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    trackers.forEach { tracker ->

                        val value = valuesState[tracker.id]

                        Text("• ${tracker.name}")

                        when (tracker.type) {

                            "number" -> Text(
                                value?.value ?: "No data",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            "bool" -> Text(
                                when (value?.value?.toBoolean()) {
                                    true -> "Yes"
                                    false -> "No"
                                    null -> "No data"
                                }
                            )

                            "text" -> Text(
                                value?.value ?: "No data"
                            )
                        }

                        if (value?.state == ValueState.UNKNOWN) {
                            Text("N/A", style = MaterialTheme.typography.labelSmall)
                        }

                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
        // =====================================================
        // EDIT SCREEN (DYNAMIC TRACKER INPUT)
        // =====================================================
        Screen.EDIT_DAY -> {

            val values = remember { mutableStateMapOf<Int, TrackerValue>() }

            Scaffold(
                bottomBar = {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Button(onClick = { screen = Screen.CALENDAR }) {
                            Text("Cancel")
                        }

                        Button(onClick = {
                            viewModel.saveEntryForDate(
                                System.currentTimeMillis(),
                                values.values.toList()
                            )
                            screen = Screen.CALENDAR
                        }) {
                            Text("Save Day")
                        }
                    }
                }
            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {

                    Text(
                        "Edit Day",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    LazyColumn {

                        items(trackers) { tracker ->

                            Text(tracker.name)

                            when (tracker.type) {

                                "number" -> {
                                    val current =
                                        values[tracker.id]?.value?.toFloatOrNull() ?: 0f

                                    Slider(
                                        value = current,
                                        onValueChange = {
                                            values[tracker.id] = TrackerValue(
                                                entryId = 0,
                                                trackerId = tracker.id,
                                                value = it.toString(),
                                                state = ValueState.ENTERED
                                            )
                                        },
                                        valueRange = 0f..10f
                                    )
                                }

                                "bool" -> {
                                    val current =
                                        values[tracker.id]?.value?.toBoolean() ?: false

                                    Checkbox(
                                        checked = current,
                                        onCheckedChange = {
                                            values[tracker.id] = TrackerValue(
                                                entryId = 0,
                                                trackerId = tracker.id,
                                                value = it.toString(),
                                                state = ValueState.ENTERED
                                            )
                                        }
                                    )
                                }

                                "text" -> {
                                    TextField(
                                        value = values[tracker.id]?.value ?: "",
                                        onValueChange = {
                                            values[tracker.id] = TrackerValue(
                                                entryId = 0,
                                                trackerId = tracker.id,
                                                value = it,
                                                state = ValueState.ENTERED
                                            )
                                        }
                                    )
                                }
                            }

                            Button(onClick = {
                                values[tracker.id] = TrackerValue(
                                    entryId = 0,
                                    trackerId = tracker.id,
                                    value = null,
                                    state = ValueState.UNKNOWN
                                )
                            }) {
                                Text("Mark as N/A")
                            }

                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

        // =====================================================
        // ADD NEW VALUE TO TRACK
        // =====================================================
        Screen.ADD_TRACKER -> {

            var name by remember { mutableStateOf("") }
            var type by remember { mutableStateOf("text") }

            Column(Modifier.fillMaxSize().padding(16.dp)) {

                Text("Add Tracker", style = MaterialTheme.typography.headlineMedium)

                Spacer(Modifier.height(12.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tracker Name") }
                )

                Spacer(Modifier.height(12.dp))

                Text("Type")

                Row {

                    Button(onClick = { type = "text" }) {
                        Text("Text")
                    }

                    Button(onClick = { type = "number" }) {
                        Text("Number")
                    }

                    Button(onClick = { type = "bool" }) {
                        Text("Yes/No")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(onClick = {
                    viewModel.addTracker(name, type)
                    screen = Screen.CALENDAR
                }) {
                    Text("Create Tracker")
                }
            }
        }
    }
}
