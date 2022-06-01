package com.victorloveday.healthio.ui.settings

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.victorloveday.healthio.R
import com.victorloveday.healthio.databinding.FragmentMyProfileBinding

class MyProfileFragment: Fragment(R.layout.fragment_my_profile) {

    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var fromBottomEffect: Animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyProfileBinding.bind(view)

        initializeAnimations()

        binding.editUserName.setOnClickListener {
            binding.editUserNameBottomSheet.visibility = View.VISIBLE
            binding.editUserNameBottomSheet.startAnimation(fromBottomEffect)

            disableItemClicks()

            binding.saveUserName.setOnClickListener {
                binding.editUserNameBottomSheet.visibility = View.GONE
                enableItemClicks()
            }

        }
        binding.editWeight.setOnClickListener {
            binding.editWeightBottomSheet.visibility = View.VISIBLE
            binding.editWeightBottomSheet.startAnimation(fromBottomEffect)

            disableItemClicks()

            binding.saveWeight.setOnClickListener {
                binding.editWeightBottomSheet.visibility = View.GONE
                enableItemClicks()
            }

        }
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