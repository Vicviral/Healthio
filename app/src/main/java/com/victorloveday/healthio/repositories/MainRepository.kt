package com.victorloveday.healthio.repositories

import com.victorloveday.healthio.database.models.Run
import com.victorloveday.healthio.database.models.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDao: RunDao
) {
    suspend fun addRun(run: Run) = runDao.addRun(run)

    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)

    fun getRunsByDate() = runDao.getRunsByDate()

    fun getRunsByTime() = runDao.getRunsByTime()

    fun getRunsByDistanceCovered() = runDao.getRunsByDistanceCovered()

    fun getRunsByBurntCalories() = runDao.getRunsByBurntCalories()

    fun getRunsByAverageSpeed() = runDao.getRunsByAverageSpeed()

    fun getTotalAverageSpeed() = runDao.getTotalAverageSpeed()

    fun getTotalBurntCalories() = runDao.getTotalBurntCalories()

    fun getTotalDistanceCovered() = runDao.getTotalDistanceCovered()

    fun getTotalTimeSpent() = runDao.getTotalTimeSpent()
}