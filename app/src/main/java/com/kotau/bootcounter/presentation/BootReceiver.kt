package com.kotau.bootcounter.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kotau.bootcounter.data.database.BootEvent
import com.kotau.bootcounter.data.repository.BootCounterRepository
import com.kotau.bootcounter.data.work.NotificationScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class BootReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                get<BootCounterRepository>().insertBootEvent(
                    BootEvent(
                        timestamp = System.currentTimeMillis()
                    )
                )
            }

            get<NotificationScheduler>().scheduleNotification()
        }
    }
}