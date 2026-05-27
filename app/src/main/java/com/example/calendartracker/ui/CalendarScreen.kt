package com.example.calendartracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
import com.example.calendartracker.data.*
import java.util.*

@Composable
fun CalendarScreen(
    entries: List<TrackerEntry>,
    trackers: List<TrackerDefinition>,
    onAddEdit: () -> Unit,
    onManageTrackers: () -> Unit,
    onSelectEntry: (TrackerEntry) -> Unit
) {

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    stringResource(R.string.calendar),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(12.dp))

                Row {
                    Button(
                        onClick = onAddEdit,
                        enabled = trackers.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.add_edit_today))
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(onClick = onManageTrackers) {
                        Text(stringResource(R.string.manage_trackers))
                    }
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            items(entries) { entry ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onSelectEntry(entry) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(Date(entry.date).toString())
                        Text(stringResource(R.string.view_details))
                    }
                }
            }
        }
    }
}