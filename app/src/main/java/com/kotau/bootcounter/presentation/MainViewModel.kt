package com.kotau.bootcounter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotau.bootcounter.data.repository.BootCounterRepository
import com.kotau.bootcounter.data.work.NotificationScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(
    private val bootCounterRepository: BootCounterRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _bootEvents = MutableStateFlow("")
    val bootEvents = _bootEvents.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val bootEvents = bootCounterRepository.getAllBootEvents()
            val bootEventsText = if (bootEvents.isEmpty()) {
                "No boots detected"
            } else {
                val eventsPerDay = bootEvents
                    .groupBy {
                        SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(
                            Date(it.timestamp)
                        )
                    }
                    .mapValues {
                        it.value.size
                    }

                eventsPerDay.entries
                    .joinToString("\n") {
                        "${it.key} - ${it.value}"
                    }
            }

            _bootEvents.value = bootEventsText
        }
    }

    fun updateDismissCount(count: String) {
        count.toIntOrNull()?.let {
            notificationScheduler.updateDismissCount(it)
        }
    }

    fun updateInterval(interval: String) {
        interval.toLongOrNull()?.let {
            notificationScheduler.updateInterval(it)
        }
    }
}