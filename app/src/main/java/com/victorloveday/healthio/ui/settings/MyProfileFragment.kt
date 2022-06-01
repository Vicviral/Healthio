package com.victorloveday.healthio.ui.settings

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.victorloveday.healthio.R
import com.victorloveday.healthio.database.UserManager
import com.victorloveday.healthio.databinding.FragmentMyProfileBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class MyProfileFragment: Fragment(R.layout.fragment_my_profile) {

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var fromBottomEffect: Animation
    private lateinit var userManager: UserManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyProfileBinding.bind(view)

        //initialize user manager
        userManager = UserManager(requireContext())

        initializeAnimations()

        observeUserData()

        //edit user name
        binding.editUserName.setOnClickListener {
            binding.editUserNameBottomSheet.visibility = View.VISIBLE
            binding.editUserNameBottomSheet.startAnimation(fromBottomEffect)

            disableItemClicks()

            binding.saveUserName.setOnClickListener {
                updateUserName()
                binding.editUserNameBottomSheet.visibility = View.GONE
                enableItemClicks()
            }

        }

        //edit user weight
        binding.editWeight.setOnClickListener {
            binding.editWeightBottomSheet.visibility = View.VISIBLE
            binding.editWeightBottomSheet.startAnimation(fromBottomEffect)

            disableItemClicks()

            binding.saveWeight.setOnClickListener {
                updateUserWeight()
                binding.editWeightBottomSheet.visibility = View.GONE
                enableItemClicks()
            }

        }

        //edit user bio
        binding.editBio.setOnClickListener {
            binding.editBioBottomSheet.visibility = View.VISIBLE
            binding.editBioBottomSheet.startAnimation(fromBottomEffect)

            disableItemClicks()

            binding.saveBio.setOnClickListener {
                updateUserBio()
                binding.editBioBottomSheet.visibility = View.GONE
                enableItemClicks()
            }

        }

        //edit user age
        binding.editAge.setOnClickListener {
            binding.editAgeBottomSheet.visibility = View.VISIBLE
            binding.editAgeBottomSheet.startAnimation(fromBottomEffect)

            disableItemClicks()

            binding.saveAge.setOnClickListener {
                updateUserAge()
                binding.editAgeBottomSheet.visibility = View.GONE
                enableItemClicks()
            }

        }

    }

    private fun updateUserName() {
        val userName = binding.userNameEdit.text.toString()

        GlobalScope.launch {
            userManager.storeUserName(userName)
        }
    }
    private fun updateUserBio() {
        val userBio = binding.bioEdit.text.toString()

        GlobalScope.launch {
            userManager.storeBio(userBio)
        }
    }
    private fun updateUserAge() {
        val userAge = binding.ageEdit.text.toString()

        GlobalScope.launch {
            userManager.storeAge(userAge.toInt())
        }
    }
    private fun updateUserWeight() {
        val userWeight = binding.weightEdit.text.toString()

        GlobalScope.launch {
            userManager.storeWeight(userWeight.toInt())
        }
    }

    private fun observeUserData() {

        userManager.userNameFlow.asLiveData().observe(viewLifecycleOwner, { un ->
            userManager.userBioFlow.asLiveData().observe(viewLifecycleOwner, { ub ->
                userManager.userAgeFlow.asLiveData().observe(viewLifecycleOwner, { ua ->
                    userManager.userWeightFlow.asLiveData().observe(viewLifecycleOwner, { uw ->

                        if (un == "") {
                            binding.userName.text = "Enter name"
                        }else {
                            binding.userName.text = un
                        }
                        if (ub == "") {
                            binding.userBio.text = "Burning some calories."
                        }else {
                            binding.userBio.text = ub
                        }
                        binding.userAge.text = ua.toString()

                        if (uw == 0) {
                            binding.userAge.text = "Enter weight"
                        }else {
                            binding.userWeight.text = "$uw kg"
                        }

                    })
                })
            })
        })
    }

    private fun disableItemClicks() {
        binding.editUserName.isClickable = false
        binding.editBio.isClickable = false
        binding.editAge.isClickable = false
        binding.editWeight.isClickable = false
    }
    private fun enableItemClicks() {
        binding.editUserName.isClickable = true
        binding.editBio.isClickable = true
        binding.editAge.isClickable = true
        binding.editWeight.isClickable = true
    }

    private fun initializeAnimations() {
        fromBottomEffect = AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom)
    }

}