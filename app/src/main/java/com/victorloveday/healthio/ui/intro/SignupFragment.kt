package com.victorloveday.healthio.ui.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.victorloveday.healthio.R
import com.victorloveday.healthio.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment: Fragment(R.layout.fragment_sign_up) {

    private val viewModel: StatisticsViewModel by viewModels()
}