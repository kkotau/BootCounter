package com.kotau.bootcounter

import android.app.Application
import com.kotau.bootcounter.di.bootCounterModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BootCounterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BootCounterApplication)
            modules(
                bootCounterModule
            )
        }
    }
}