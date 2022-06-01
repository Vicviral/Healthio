package com.victorloveday.healthio.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.victorloveday.healthio.R
import com.victorloveday.healthio.database.UserManager
import com.victorloveday.healthio.databinding.ActivitySetttingsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetttingsBinding
    lateinit var userManager: UserManager
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Healthio)
        binding = ActivitySetttingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize user manager
        userManager = UserManager(this)

        //set up toolbar
        val toolbar = binding.toolbar as Toolbar
        setSupportActionBar(toolbar);

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener {
            finish()
        }

        //initialize nav host fragment
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentSettings) as NavHostFragment

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.runFragment, R.id.workoutFragment, R.id.peopleFragment)
        )

        //Toolbar
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController, appBarConfiguration)

    }


}