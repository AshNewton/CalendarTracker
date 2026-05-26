package com.example.calendartracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
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
                Button(onClick = onBack) { Text(stringResource(R.string.back)) }
                Button(onClick = onEdit) { Text(stringResource(R.string.edit)) }
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
                Text(stringResource(R.string.error_no_entry_selected))
                return@Column
            }

            Text(stringResource(R.string.day_details), style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(12.dp))

            Text(stringResource(R.string.format_date, Date(entry.date)))

            Spacer(Modifier.height(16.dp))

            trackers.forEach { tracker ->

                val value = valuesState[tracker.id]

                Text(tracker.name)

                Text(value?.value ?: stringResource(R.string.no_data))

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}