package com.victorloveday.healthio.ui.fragments.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.victorloveday.healthio.R
import com.victorloveday.healthio.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private val viewModel: StatisticsViewModel by viewModels()
}