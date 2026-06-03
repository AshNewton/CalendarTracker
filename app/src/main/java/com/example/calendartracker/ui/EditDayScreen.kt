package com.example.calendartracker.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
import com.example.calendartracker.data.*
import com.example.calendartracker.ui.components.TrackerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDayScreen(
    entry: TrackerEntry?,
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    val editDate = entry?.date ?: System.currentTimeMillis()

    val values = remember {
        mutableStateMapOf<Int, TrackerValue>()
    }

    LaunchedEffect(editDate) {
        viewModel.loadValuesForDay(
            editDate
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
                        editDate,
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
                TrackerCard(
                    tracker = tracker,
                    value = values[tracker.id],
                    onValueChanged = {
                        values[tracker.id] = TrackerValue(
                            entryId = 0,
                            trackerId = tracker.id,
                            value = it,
                            state = ValueState.ENTERED
                        )
                    },
                    onClear = {
                        values[tracker.id] = TrackerValue(
                            entryId = 0,
                            trackerId = tracker.id,
                            value = null,
                            state = ValueState.UNKNOWN
                        )
                    }
                )
            }
        }
    }
}