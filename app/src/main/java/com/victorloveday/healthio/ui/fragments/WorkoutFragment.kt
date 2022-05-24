package com.victorloveday.healthio.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.victorloveday.healthio.R
import com.victorloveday.healthio.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutFragment: Fragment(R.layout.fragment_workout) {

    private val viewModel: StatisticsViewModel by viewModels()
}