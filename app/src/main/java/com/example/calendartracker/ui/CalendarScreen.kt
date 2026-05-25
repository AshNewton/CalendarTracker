package com.example.calendartracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.*
import java.util.*

@Composable
fun CalendarScreen(
    entries: List<TrackerEntry>,
    onAddEdit: () -> Unit,
    onAddTracker: () -> Unit,
    onSelectEntry: (TrackerEntry) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Calendar", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        Button(onClick = onAddEdit) {
            Text("Add / Edit Today")
        }

        Button(onClick = onAddTracker) {
            Text("Add New Tracker")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(entries) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onSelectEntry(entry) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(Date(entry.date).toString())
                        Text("Tap to view details")
                    }
                }
            }
        }
    }
}