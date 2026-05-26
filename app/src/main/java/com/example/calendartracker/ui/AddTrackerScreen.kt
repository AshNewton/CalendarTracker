package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*
import com.example.calendartracker.R

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

                Button(
                    onClick = {
                        viewModel.addTracker(
                            name,
                            type,
                            minValue.toInt(),
                            maxValue.toInt()
                        ) { result ->
                            result.onSuccess {
                                onDone()
                            }
                            result.onFailure {
                                error = it.message
                            }
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
                .padding(16.dp)
        ) {

            Text(
                stringResource(R.string.add_tracker),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(12.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.tracker_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(stringResource(R.string.type))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (type == TrackerType.TEXT) {
                    Button(onClick = { type = TrackerType.TEXT }) {
                        Text(stringResource(R.string.text))
                    }
                } else {
                    OutlinedButton(onClick = { type = TrackerType.TEXT }) {
                        Text(stringResource(R.string.text))
                    }
                }

                if (type == TrackerType.NUMBER) {
                    Button(onClick = { type = TrackerType.NUMBER }) {
                        Text(stringResource(R.string.number))
                    }
                } else {
                    OutlinedButton(onClick = { type = TrackerType.NUMBER }) {
                        Text(stringResource(R.string.number))
                    }
                }

                if (type == TrackerType.BOOL) {
                    Button(onClick = { type = TrackerType.BOOL }) {
                        Text(stringResource(R.string.yes_no))
                    }
                } else {
                    OutlinedButton(onClick = { type = TrackerType.BOOL }) {
                        Text(stringResource(R.string.yes_no))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (type == TrackerType.NUMBER) {

                TextField(
                    value = minValue,
                    onValueChange = { minValue = it },
                    label = { Text(stringResource(R.string.min_value)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = maxValue,
                    onValueChange = { maxValue = it },
                    label = { Text(stringResource(R.string.max_value)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
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