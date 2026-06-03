package com.example.calendartracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.data.TrackerEntry
import java.text.DateFormat
import java.util.Date
import com.example.calendartracker.R
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerValue
import com.example.calendartracker.util.buildMonthGrid
import com.example.calendartracker.util.buildTrackerValueMap
import com.example.calendartracker.util.dayKey
import com.example.calendartracker.util.getHeatColor
import com.example.calendartracker.util.isToday
import java.time.YearMonth

@Composable
fun CalendarList(
    month: YearMonth,
    entries: List<TrackerEntry>,
    onDayClick: (Long) -> Unit,
    selectedTrackerId: Int?,
    trackers: List<TrackerDefinition>,
    values: List<TrackerValue>
) {
    val days = remember(month) {
        buildMonthGrid(month)
    }

    val entryKeys = remember(entries) {
        entries.map { it.dayKey }.toSet()
    }

    val valueMap = remember(values, entries, selectedTrackerId) {
        buildTrackerValueMap(
            values = values,
            entries = entries,
            trackerId = selectedTrackerId
        )
    }

    val tracker = trackers.firstOrNull { it.id == selectedTrackerId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        items(days) { day ->
            val dayKey = day?.let {
                dayKey(month.year, month.monthValue, it)
            }

            val hasEntry = dayKey != null && entryKeys.contains(dayKey)

            if (hasEntry) {
                val value = valueMap[dayKey]

                val heatColor = getHeatColor(tracker, value)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onDayClick(dayKey)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = heatColor
                    ),
                    border = if (isToday(dayKey))
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else null
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = DateFormat.getDateInstance(DateFormat.FULL)
                                .format(Date(dayKey)),
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (tracker==null)
                            Text(
                                text = stringResource(R.string.view_details),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        else value?.value?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}