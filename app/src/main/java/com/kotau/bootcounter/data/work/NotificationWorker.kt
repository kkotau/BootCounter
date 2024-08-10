package com.kotau.bootcounter.data.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kotau.bootcounter.presentation.NotificationDismissReceiver
import com.kotau.bootcounter.R
import com.kotau.bootcounter.data.repository.BootCounterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params), KoinComponent {

    private val bootCounterRepository = get<BootCounterRepository>()

    override fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {
            val bootEvents = bootCounterRepository.getLastTwoBootEvents()

            val notificationText = when {
                bootEvents.isEmpty() -> "No boots detected"
                bootEvents.size == 1 -> {
                    val date = SimpleDateFormat(
                        "dd/MM/yyyy HH:MM:SS",
                        Locale.getDefault()
                    ).format(
                        Date(bootEvents.first().timestamp)
                    )

                    "The boot was detected = $date"
                }
                else -> {
                    val delta = bootEvents[0].timestamp - bootEvents[1].timestamp
                    "Last boots time delta = ${delta / 1000} seconds"
                }
            }

            withContext(Dispatchers.Main) {
                showNotification(notificationText)
            }
        }

        return Result.success()
    }

    private fun showNotification(text: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "boot_event_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Boot Events"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Boot Event")
            .setContentText(text)
            .setAutoCancel(true)
            .setDeleteIntent(createDismissIntent())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun createDismissIntent(): PendingIntent {
        val intent = Intent(
            applicationContext,
            NotificationDismissReceiver::class.java
        )
        return PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}
