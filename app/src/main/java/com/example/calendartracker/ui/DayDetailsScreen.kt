package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*
import java.util.*

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
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onBack) { Text("Back") }
                Button(onClick = onEdit) { Text("Edit") }
            }
        }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            if (entry == null) {
                Text("No entry selected")
                return@Column
            }

            Text("Day Details", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(12.dp))

            Text("Date: ${Date(entry.date)}")

            Spacer(Modifier.height(16.dp))

            trackers.forEach { tracker ->

                val value = valuesState[tracker.id]

                Text("• ${tracker.name}")

                Text(value?.value ?: "No data")

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}