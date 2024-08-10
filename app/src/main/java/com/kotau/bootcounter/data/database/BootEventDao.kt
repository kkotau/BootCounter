package com.kotau.bootcounter.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BootEventDao {
    @Insert
    suspend fun insertBootEvent(event: BootEvent)

    @Query("SELECT * FROM boot_events ORDER BY timestamp DESC")
    suspend fun getAllBootEvents(): List<BootEvent>

    @Query("SELECT * FROM boot_events ORDER BY timestamp DESC LIMIT 2")
    suspend fun getLastTwoBootEvents(): List<BootEvent>
}