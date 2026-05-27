package com.example.calendartracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.calendartracker.R
import com.example.calendartracker.data.TrackerDefinition
import com.example.calendartracker.data.TrackerEntry
import com.example.calendartracker.ui.components.CalendarGrid
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: MainViewModel,
    entries: List<TrackerEntry>,
    trackers: List<TrackerDefinition>,
    onAddEdit: () -> Unit,
    onManageTrackers: () -> Unit,
    onSelectEntry: (TrackerEntry?) -> Unit
) {
    val isCalendarView by viewModel.isCalendarView.collectAsState()

    val startMonth = remember { YearMonth.of(2020, 1) }
    val currentMonth = remember { YearMonth.now() }

    val startIndex = remember {
        ChronoUnit.MONTHS.between(startMonth, currentMonth).toInt()
    }

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { 2400 }
    )

    fun pageToMonth(page: Int): YearMonth {
        return startMonth.plusMonths(page.toLong())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.calendar)) },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleCalendarView() }
                    ) {
                        Icon(
                            imageVector =
                                if (isCalendarView)
                                    Icons.AutoMirrored.Filled.List
                                else
                                    Icons.Default.CalendarMonth,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAddEdit,
                    enabled = trackers.isNotEmpty()
                ) {
                    Text(stringResource(R.string.add_edit_today))
                }

                Button(onClick = onManageTrackers) {
                    Text(stringResource(R.string.manage_trackers))
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                if (!isCalendarView) { // list view
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
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
                } else { // calendar view
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize(),
                        pageSpacing = 8.dp,
                        beyondViewportPageCount = 1
                    ) { page ->
                        val month = pageToMonth(page)

                        val monthName = remember(month) {
                            month.month.name.lowercase().replaceFirstChar { it.uppercase() }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "$monthName ${month.year}",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(12.dp)
                            )

                            CalendarGrid(
                                month = month,
                                entries = entries,
                                onDayClick = { daykey ->
                                    val entry = entries.firstOrNull {
                                        it.dayKey == daykey
                                    }

                                    onSelectEntry(entry)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}