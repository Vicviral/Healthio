package com.victorloveday.healthio.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.victorloveday.healthio.R
import com.victorloveday.healthio.database.models.RunDao
import com.victorloveday.healthio.databinding.ActivityMainBinding
import com.victorloveday.healthio.utils.constants.Constant.SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize nav host fragment
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        //navigate to tracking fragment when click from notification
        navigateToTrackingFragmentIfNeeded(intent)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.runFragment, R.id.workoutFragment, R.id.peopleFragment)
        )


        //Toolbar
        navController = navHostFragment.findNavController()

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //bottom nav bar
        binding.bottomNavigationView.setupWithNavController(navController)


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent != null) {
            if(intent.action == SHOW_TRACKING_FRAGMENT) {
                navHostFragment.findNavController().navigate(R.id.action_global_run_tracking_fragment)
            }
        }
    }
}