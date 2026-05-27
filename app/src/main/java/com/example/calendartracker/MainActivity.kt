package com.example.calendartracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendartracker.data.AppDatabase
import com.example.calendartracker.data.SettingsDataStore
import com.example.calendartracker.data.SettingsRepository
import com.example.calendartracker.repository.TrackerRepository
import com.example.calendartracker.ui.MainViewModel
import com.example.calendartracker.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(applicationContext).dao()
        val trackerRepository = TrackerRepository(dao)

        val settingsDataStore = SettingsDataStore(applicationContext)
        val settingsRepository = SettingsRepository(settingsDataStore)

        setContent {
            val vm: MainViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(trackerRepository, settingsRepository) as T
                }
            })

            MainScreen(vm)
        }
    }
}