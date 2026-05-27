package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
import com.example.calendartracker.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrackerScreen(
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TrackerType.TEXT) }

    var minValue by remember { mutableStateOf("0") }
    var maxValue by remember { mutableStateOf("10") }

    var error by remember { mutableStateOf<String?>(null) }

    val nameInUse = trackers.any {
        it.name.equals(name, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_tracker)) },
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
                Spacer(Modifier.weight(1f))

                TextButton(onClick = onCancel) {
                    Text(stringResource(R.string.cancel))
                }

                Button(
                    onClick = {
                        viewModel.addTracker(
                            name,
                            type,
                            minValue.toInt(),
                            maxValue.toInt()
                        ) { result ->
                            result.onSuccess { onDone() }
                            result.onFailure { error = it.message }
                        }
                    },
                    enabled = name.isNotBlank() && !nameInUse
                ) {
                    Text(stringResource(R.string.create))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.tracker_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = stringResource(R.string.type),
                style = MaterialTheme.typography.titleMedium
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = type == TrackerType.TEXT,
                    onClick = { type = TrackerType.TEXT },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = TrackerType.entries.size
                    ),
                ) {
                    Text(stringResource(R.string.text))
                }

                SegmentedButton(
                    selected = type == TrackerType.NUMBER,
                    onClick = { type = TrackerType.NUMBER },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = TrackerType.entries.size
                    ),
                ) {
                    Text(stringResource(R.string.number))
                }

                SegmentedButton(
                    selected = type == TrackerType.BOOL,
                    onClick = { type = TrackerType.BOOL },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 2,
                        count = TrackerType.entries.size
                    ),
                ) {
                    Text(stringResource(R.string.yes_no))
                }
            }

            if (type == TrackerType.NUMBER) {
                Text(
                    text = "Range",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = minValue,
                    onValueChange = { minValue = it },
                    label = { Text(stringResource(R.string.min_value)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = maxValue,
                    onValueChange = { maxValue = it },
                    label = { Text(stringResource(R.string.max_value)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (nameInUse) {
                Text(
                    text = stringResource(R.string.error_duplicate_tracker_name),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}