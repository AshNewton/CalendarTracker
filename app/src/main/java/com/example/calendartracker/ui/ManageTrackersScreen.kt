package com.example.calendartracker.ui

import androidx.compose.ui.res.stringResource
import com.example.calendartracker.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*

@Composable
fun ManageTrackersScreen(
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {

                Button(onClick = onBack) {
                    Text(stringResource(R.string.back))
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
                stringResource(R.string.manage_trackers),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(trackers) { tracker ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                tracker.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(stringResource(R.string.format_type, tracker.type.displayName))

                            if (tracker.type == TrackerType.NUMBER &&
                                tracker.minValue != null && tracker.maxValue != null) {
                                Text(
                                    stringResource(R.string.format_range_int,
                                        tracker.minValue, tracker.maxValue
                                    )
                                )
                            }                            }

                            Spacer(Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = {
                                    viewModel.deleteTracker(tracker)
                                }
                            ) {
                                Text(stringResource(R.string.delete))
                            }
                        }
                    }
                }
            }
        }
    }

