package com.example.calendartracker.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
import com.example.calendartracker.data.*

@OptIn(ExperimentalMaterial3Api::class)
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

    BackHandler(onBack = onCancel)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_day)) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                TextButton(onClick = onCancel) {
                    Text(stringResource(R.string.cancel))
                }

                Spacer(Modifier.weight(1f))

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.track_your_day),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            items(trackers) { tracker ->
                val currentValue = values[tracker.id]

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        when (tracker.type) {
                            TrackerType.NUMBER -> {
                                val min = tracker.minValue ?: 0
                                val max = tracker.maxValue ?: 10

                                val current = currentValue?.value?.toFloatOrNull()
                                    ?: min.toFloat()

                                Text(
                                    tracker.name,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = current.toInt().toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
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
                            }
                            TrackerType.BOOL -> {
                                val checked = currentValue?.value?.toBoolean() ?: false

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        tracker.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(Modifier.width(12.dp))

                                    Switch(
                                        checked = checked,
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
                            }
                            TrackerType.TEXT -> {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = currentValue?.value ?: "",
                                        onValueChange = {
                                            values[tracker.id] = TrackerValue(
                                                entryId = 0,
                                                trackerId = tracker.id,
                                                value = it,
                                                state = ValueState.ENTERED
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        placeholder = {
                                            Text(stringResource(R.string.format_placeholder,tracker.name.lowercase()))
                                        }
                                    )
                                }
                            }
                        }

                        val isNA = currentValue?.state == ValueState.UNKNOWN

                        if (isNA) {
                            Button(
                                onClick = {
                                    values[tracker.id] = TrackerValue(
                                        entryId = 0,
                                        trackerId = tracker.id,
                                        value = "",
                                        state = ValueState.ENTERED
                                    )
                                }
                            ) {
                                Text(stringResource(R.string.mark_as_filled))
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    values[tracker.id] = TrackerValue(
                                        entryId = 0,
                                        trackerId = tracker.id,
                                        value = null,
                                        state = ValueState.UNKNOWN
                                    )
                                }
                            ) {
                                Text(stringResource(R.string.mark_as_na))
                            }
                        }
                    }
                }
            }
        }
    }
}