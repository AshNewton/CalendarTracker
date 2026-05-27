package com.example.calendartracker.ui

import androidx.compose.ui.res.stringResource
import com.example.calendartracker.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*
import com.example.calendartracker.ui.components.SwipeReveal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTrackersScreen(
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onAddTracker: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.manage_trackers)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTracker,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_tracker)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(
                    items = trackers,
                    key = {it.id}
                ) { tracker ->
                    SwipeReveal(
                        actionsWidth = 160f,
                        actions = { progress ->
                            val scale = 0.8f + (0.2f * progress)

                            IconButton(
                                onClick = { viewModel.deleteTracker(tracker) },
                                modifier = Modifier.graphicsLayer {
                                    this.alpha = progress
                                    this.scaleX = scale
                                    this.scaleY = scale
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Column(Modifier.padding(8.dp)) {
                                Text(tracker.name, style = MaterialTheme.typography.titleMedium)

                                Text(stringResource(R.string.format_type, tracker.type.displayName))

                                if (tracker.type == TrackerType.NUMBER &&
                                    tracker.minValue != null &&
                                    tracker.maxValue != null
                                ) {
                                    Text(
                                        stringResource(
                                            R.string.format_range_int,
                                            tracker.minValue,
                                            tracker.maxValue
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

