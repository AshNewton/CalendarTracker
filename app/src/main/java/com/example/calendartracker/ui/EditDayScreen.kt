package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*

@Composable
fun EditDayScreen(
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
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

                Button(onClick = onCancel) {
                    Text("Cancel")
                }

                Button(onClick = {
                    viewModel.saveEntryForDate(
                        System.currentTimeMillis(),
                        values.values.toList()
                    )
                    onSave()
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
                            val min = tracker.minValue ?: 0
                            val max = tracker.maxValue ?: 10

                            val current = values[tracker.id]?.value?.toFloatOrNull()
                                ?: min.toFloat()


                            Text(
                                text = "Value: ${current.toInt()}",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Slider(
                                value = current,
                                onValueChange = {
                                    values[tracker.id] = TrackerValue(
                                        entryId = 0,
                                        trackerId = tracker.id,
                                        value = it.toInt().toString(),
                                        state = ValueState.ENTERED
                                    )
                                },
                                valueRange = min.toFloat()..max.toFloat()
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