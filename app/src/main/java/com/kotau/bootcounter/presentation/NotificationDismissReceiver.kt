package com.kotau.bootcounter.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kotau.bootcounter.data.work.NotificationScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class NotificationDismissReceiver : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent) {
        get<NotificationScheduler>().dismissNotification()
    }
}