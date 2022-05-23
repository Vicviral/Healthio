package com.victorloveday.healthio.database.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY date DESC")
    fun getRunsByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distanceCovered")
    fun getRunsByDistanceCovered(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY burntCalories DESC")
    fun getRunsByBurntCalories(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY duration")
    fun getRunsByDuration(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY averageSpeed")
    fun getRunsByAverageSpeed(): LiveData<List<Run>>

}