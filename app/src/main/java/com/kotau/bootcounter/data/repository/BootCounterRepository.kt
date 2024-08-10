package com.kotau.bootcounter.data.repository

import com.kotau.bootcounter.data.database.BootEvent
import com.kotau.bootcounter.data.database.BootEventDao

class BootCounterRepository(
    private val bootEventDao: BootEventDao
) {

    suspend fun insertBootEvent(event: BootEvent) {
        bootEventDao.insertBootEvent(event)
    }

    suspend fun getAllBootEvents(): List<BootEvent> =
        bootEventDao.getAllBootEvents()

    suspend fun getLastTwoBootEvents(): List<BootEvent> =
        bootEventDao.getLastTwoBootEvents()
}