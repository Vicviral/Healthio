package com.victorloveday.healthio.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.victorloveday.healthio.repositories.MainRepository

class StatisticsViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val totalTimeRun = mainRepository.getTotalTimeSpent()
    val totalDistanceRun = mainRepository.getTotalDistanceCovered()
    val totalCaloriesBurnt = mainRepository.getTotalBurntCalories()
    val totalAverageSpeed = mainRepository.getTotalAverageSpeed()

    val sortRunByDate = mainRepository.getRunsByDate()
}