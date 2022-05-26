package com.victorloveday.healthio.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victorloveday.healthio.database.models.Run
import com.victorloveday.healthio.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    fun addRun(run: Run) = viewModelScope.launch {
        mainRepository.addRun(run)
    }
}