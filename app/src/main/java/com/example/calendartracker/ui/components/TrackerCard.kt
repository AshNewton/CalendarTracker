package com.example.calendartracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerType
import com.example.calendartracker.data.TrackerValue

@Composable
fun TrackerCard(
    tracker: TrackerDefinition,
    value: TrackerValue?,
    onValueChanged: (String) -> Unit,
    onClear: () -> Unit,
    ) {

    Card(
        modifier = Modifier.fillMaxWidth()
    )
    {
        when (tracker.type) {
            TrackerType.NUMBER -> {
                val min = tracker.minValue ?: 0
                val max = tracker.maxValue ?: 10

                val current = value?.value?.toFloatOrNull()
                    ?: min.toFloat()

                Column (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        tracker.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = current.toInt().toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row (verticalAlignment = Alignment.CenterVertically) {
                            Slider(
                                modifier = Modifier.weight(1f),
                                value = current,
                                onValueChange = {
                                    onValueChanged(it.toInt().toString())
                                },
                                valueRange = min.toFloat()..max.toFloat()
                            )
                            IconButton(
                                onClick = onClear
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            TrackerType.BOOL -> {
                val checked = value?.value?.toBoolean() ?: false

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tracker.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.width(12.dp))

                        Switch(
                            checked = checked,
                            onCheckedChange = {
                                onValueChanged(it.toString())
                            }
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            TrackerType.TEXT -> {
                Row (modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = value?.value ?: "",
                        onValueChange = onValueChanged,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(tracker.name.lowercase())
                        },
                        trailingIcon = {
                            IconButton(onClick = onClear) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}