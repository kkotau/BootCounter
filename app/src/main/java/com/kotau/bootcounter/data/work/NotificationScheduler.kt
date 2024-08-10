package com.kotau.bootcounter.data.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

private const val DEFAULT_MAX_DISMISS_COUNT = 5
private const val DEFAULT_REPEAT_INTERVAL = 15L

class NotificationScheduler(
    private val workManager: WorkManager
) {
    private var currentDismissCount = 0

    private var maxDismissCount = DEFAULT_MAX_DISMISS_COUNT
    private var repeatInterval = DEFAULT_REPEAT_INTERVAL

    fun updateDismissCount(newCount: Int) {
        maxDismissCount = newCount
        scheduleNotification()
    }

    fun updateInterval(newInterval: Long) {
        repeatInterval = newInterval
        scheduleNotification()
    }

    fun scheduleNotification() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval,
            TimeUnit.MINUTES
        )
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "BootNotificationWork",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                workRequest
            )
    }

    fun dismissNotification() {
        currentDismissCount++

        repeatInterval = if (currentDismissCount > maxDismissCount) {
            DEFAULT_REPEAT_INTERVAL
        } else {
            currentDismissCount * 20L
        }

        scheduleNotification()
    }
}