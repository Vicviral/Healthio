package com.victorloveday.healthio.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.victorloveday.healthio.R
import com.victorloveday.healthio.database.UserManager
import com.victorloveday.healthio.databinding.FragmentSetupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
@AndroidEntryPoint
class SetupFragment: Fragment(R.layout.fragment_setup) {

    private lateinit var binding: FragmentSetupBinding
    lateinit var userManager: UserManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupBinding.bind(view)

        //initialize user manager
        userManager = UserManager(requireContext())


        //navigate to profile fragment
        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_setupFragment_to_myProfileFragment)
        }


    }


}