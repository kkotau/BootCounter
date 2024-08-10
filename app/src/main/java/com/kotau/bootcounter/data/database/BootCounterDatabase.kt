package com.kotau.bootcounter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BootEvent::class], version = 1)
abstract class BootCounterDatabase : RoomDatabase() {
    abstract fun bootEventDao(): BootEventDao
}