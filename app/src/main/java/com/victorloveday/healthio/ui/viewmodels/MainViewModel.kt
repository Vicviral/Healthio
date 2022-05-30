package com.victorloveday.healthio.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorloveday.healthio.database.models.Run
import com.victorloveday.healthio.repositories.MainRepository
import com.victorloveday.healthio.utils.SortRunType
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val runsSortedByDate = mainRepository.getRunsByDate()
    val runsSortedByTime = mainRepository.getRunsByTime()
    val runsSortedByDistance = mainRepository.getRunsByDistanceCovered()
    val runsSortedByAverageSpeed = mainRepository.getRunsByAverageSpeed()
    val runsSortedByCaloriesBurnt = mainRepository.getRunsByBurntCalories()

    fun addRun(run: Run) = viewModelScope.launch {
        mainRepository.addRun(run)
    }
}