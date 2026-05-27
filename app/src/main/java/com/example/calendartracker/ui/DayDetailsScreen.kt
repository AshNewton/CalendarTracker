package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
import com.example.calendartracker.data.*
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailScreen(
    entry: TrackerEntry?,
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {

    val valuesState = remember { mutableStateMapOf<Int, TrackerValue>() }

    LaunchedEffect(entry?.id) {
        entry?.let {
            viewModel.getValuesForEntry(it.id) { values ->
                valuesState.clear()
                values.forEach {
                    valuesState[it.trackerId] = it
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.day_details)) },
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
        bottomBar = {
            BottomAppBar {
                Spacer(Modifier.weight(1f))

                TextButton(onClick = onBack) {
                    Text(stringResource(R.string.back))
                }

                Button(onClick = onEdit) {
                    Text(stringResource(R.string.edit))
                }
            }
        }
    ) { padding ->
        if (entry == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(stringResource(R.string.error_no_entry_selected))
            }
            return@Scaffold
        }

        val dateText = remember(entry.date) {
            DateFormat.getDateInstance().format(Date(entry.date))
        }

        val visibleTrackers = trackers.filter { tracker ->
            valuesState.containsKey(tracker.id)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = dateText,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = "Daily tracker summary",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            items(visibleTrackers) { tracker ->
                val value = valuesState[tracker.id]

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(tracker.name)
                        },
                        supportingContent = {
                            if (tracker.type != TrackerType.BOOL) {
                                Text(value?.value ?: stringResource(R.string.no_data))
                            }
                        },
                        leadingContent = {
                            if (tracker.type == TrackerType.BOOL && value!= null) {
                                Icon(
                                    imageVector = if (value.value.toBoolean())
                                        Icons.Default.Check
                                    else
                                        Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (value.value.toBoolean())
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}