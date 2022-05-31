package com.victorloveday.healthio.ui.intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.victorloveday.healthio.database.UserManager
import com.victorloveday.healthio.databinding.ActivitySetupBinding
import dagger.hilt.android.AndroidEntryPoint
import android.view.View
import android.widget.Toast


@AndroidEntryPoint
class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding
    lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize user manager
        userManager = UserManager(this)

        val toolbar = binding.toolbar as Toolbar
        setSupportActionBar(toolbar);

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }


}