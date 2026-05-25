package com.example.calendartracker.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*
import java.util.*

@Composable
fun AddTrackerScreen(
    trackers: List<TrackerDefinition>,
    viewModel: MainViewModel,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("text") }

    var minValue by remember { mutableStateOf("0") }
    var maxValue by remember { mutableStateOf("10") }

    var error by remember { mutableStateOf<String?>(null) }

    val nameInUse = trackers.any { it.name.equals(name, ignoreCase = true) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Add Tracker", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Tracker Name") }
        )

        Spacer(Modifier.height(12.dp))

        Text("Type")

        Row {

            Button(onClick = { type = "text" }) {
                Text("Text")
            }

            Button(onClick = { type = "number" }) {
                Text("Number")
            }

            Button(onClick = { type = "bool" }) {
                Text("Yes/No")
            }
        }

        if (type == "number") {

            TextField(
                value = minValue,
                onValueChange = { minValue = it },
                label = { Text("Min Value") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            TextField(
                value = maxValue,
                onValueChange = { maxValue = it },
                label = { Text("Max Value") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            onCancel()
        }) {
            Text("Cancel")
        }
        Button(onClick = {
                viewModel.addTracker(name, type, minValue.toInt(), maxValue.toInt()) { result ->
                    result.onSuccess {
                        onDone()
                    }
                    result.onFailure {
                        error = it.message
                    }
                }
            },
            enabled = name.isNotBlank() && !nameInUse)
        {
            Text("Create Tracker")
        }

        error?.let {
            Spacer(Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        if(nameInUse){
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Tracker name must be unique",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
