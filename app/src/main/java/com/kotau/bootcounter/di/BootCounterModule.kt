package com.kotau.bootcounter.di

import androidx.room.Room
import androidx.work.WorkManager
import com.kotau.bootcounter.data.database.BootCounterDatabase
import com.kotau.bootcounter.data.work.NotificationScheduler
import com.kotau.bootcounter.data.repository.BootCounterRepository
import com.kotau.bootcounter.presentation.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bootCounterModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            BootCounterDatabase::class.java,
            "boot_event_database"
        ).build()
    }

    single {
        get<BootCounterDatabase>().bootEventDao()
    }

    single {
        WorkManager.getInstance(
            androidContext()
        )
    }

    single {
        NotificationScheduler(
            workManager = get()
        )
    }

    single {
        BootCounterRepository(
            bootEventDao = get()
        )
    }

    viewModel {
        MainViewModel(
            bootCounterRepository = get(),
            notificationScheduler = get()
        )
    }
}