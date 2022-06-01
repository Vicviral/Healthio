package com.victorloveday.healthio.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.victorloveday.healthio.R
import com.victorloveday.healthio.database.UserManager
import com.victorloveday.healthio.databinding.FragmentSetupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.math.round

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

        observerUserData()

        //navigate to profile fragment
        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_setupFragment_to_myProfileFragment)
        }


    }


    private fun observerUserData() {
        userManager.userNameFlow.asLiveData().observe(viewLifecycleOwner, { userName ->
            binding.userName.text = userName
        })

        userManager.userBioFlow.asLiveData().observe(viewLifecycleOwner, { userBio ->
            if (userBio == "") {
                binding.userBio.text = "Burning some calories."
            }else {
                binding.userBio.text = userBio
            }
        })
    }


    override fun onResume() {
        super.onResume()

        observerUserData()
    }

}