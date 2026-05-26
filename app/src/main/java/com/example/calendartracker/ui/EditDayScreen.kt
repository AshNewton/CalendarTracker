package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
import com.example.calendartracker.data.*

@Composable
fun EditDayScreen(
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    val values = remember {
        mutableStateMapOf<Int, TrackerValue>()
    }

    LaunchedEffect(Unit) {
        viewModel.loadValuesForDay(
            System.currentTimeMillis()
        ) { loaded ->
            values.clear()
            loaded.forEach {
                values[it.trackerId] = it
            }
        }
    }

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
                    Text(stringResource(R.string.cancel))
                }

                Button(onClick = {
                    viewModel.saveEntryForDate(
                        System.currentTimeMillis(),
                        values.values.toList()
                    )
                    onSave()
                }) {
                    Text(stringResource(R.string.save_day))
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
                stringResource(R.string.edit_day),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn {
                items(trackers) { tracker ->
                    Text(tracker.name)

                    when (tracker.type) {

                        TrackerType.NUMBER -> {
                            val min = tracker.minValue ?: 0
                            val max = tracker.maxValue ?: 10

                            val current = values[tracker.id]?.value?.toFloatOrNull()
                                ?: min.toFloat()


                            Text(
                                text = stringResource(R.string.format_value, current.toInt()),
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

                        TrackerType.BOOL -> {
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

                        TrackerType.TEXT -> {
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

                    val currentValue = values[tracker.id]
                    val isNA = currentValue?.state == ValueState.UNKNOWN

                    if (isNA) {
                        Button(onClick = {
                            values[tracker.id] = TrackerValue(
                                entryId = 0,
                                trackerId = tracker.id,
                                value = "",
                                state = ValueState.ENTERED
                            )
                        }) {
                            Text(stringResource(R.string.na))
                        }
                    } else {
                        OutlinedButton(onClick = {
                            values[tracker.id] = TrackerValue(
                                entryId = 0,
                                trackerId = tracker.id,
                                value = null,
                                state = ValueState.UNKNOWN
                            )
                        }) {
                            Text(stringResource(R.string.na))
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }

}