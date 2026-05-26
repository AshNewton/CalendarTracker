package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*

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
                    Text("Cancel")
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
                    Text("Create")
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
                "Add Tracker",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(12.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tracker Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text("Type")

            Row {

                Button(onClick = { type = TrackerType.TEXT }) {
                    Text("Text")
                }

                Spacer(Modifier.width(8.dp))

                Button(onClick = { type = TrackerType.NUMBER }) {
                    Text("Number")
                }

                Spacer(Modifier.width(8.dp))

                Button(onClick = { type = TrackerType.BOOL }) {
                    Text("Yes/No")
                }
            }

            Spacer(Modifier.height(12.dp))

            if (type == TrackerType.NUMBER) {

                TextField(
                    value = minValue,
                    onValueChange = { minValue = it },
                    label = { Text("Min Value") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = maxValue,
                    onValueChange = { maxValue = it },
                    label = { Text("Max Value") },
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
                    text = "Tracker name must be unique",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}