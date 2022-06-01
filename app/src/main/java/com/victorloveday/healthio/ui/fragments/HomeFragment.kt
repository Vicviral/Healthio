package com.victorloveday.healthio.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.victorloveday.healthio.R
import com.victorloveday.healthio.database.UserManager
import com.victorloveday.healthio.databinding.FragmentHomeBinding
import com.victorloveday.healthio.ui.settings.SettingsActivity
import com.victorloveday.healthio.ui.viewmodels.StatisticsViewModel
import com.victorloveday.healthio.utils.DashboardMarkerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import kotlin.math.round
import android.widget.Toast
import java.util.*


@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewModel: StatisticsViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var menu: Menu? = null
    private lateinit var userManager: UserManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        //initialize user manager
        userManager = UserManager(requireContext())

        observerUserData()

        //setup analytics graph
        setupAnalyticsGraph()
    }

    private fun setupAnalyticsGraph() {
        binding.analyticsChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            axisLineColor = ContextCompat.getColor(requireContext(), R.color.primaryGray)
            textColor = ContextCompat.getColor(requireContext(), R.color.primaryGray)
            setDrawGridLines(false)
        }
        binding.analyticsChart.axisLeft.apply {
            axisLineColor = ContextCompat.getColor(requireContext(), R.color.primaryGray)
            textColor = ContextCompat.getColor(requireContext(), R.color.primaryGray)
            setDrawGridLines(false)
        }
        binding.analyticsChart.axisRight.apply {
            axisLineColor = ContextCompat.getColor(requireContext(), R.color.primaryGray)
            textColor = ContextCompat.getColor(requireContext(), R.color.primaryGray)
            setDrawGridLines(false)
        }
        binding.analyticsChart.apply {
            description.text = "Average speed over time"
            legend.isEnabled = false
        }
    }

    private fun observerUserData() {
        //user name
        userManager.userNameFlow.asLiveData().observe(viewLifecycleOwner, { userName ->
            binding.userName.text = userName
        })

        viewModel.totalCaloriesBurnt.observe(viewLifecycleOwner, { calories ->
            calories?.let {
                binding.totalCalories.text = "$calories"+"kcal"
            }
        })

        viewModel.totalDistanceRun.observe(viewLifecycleOwner, { distance ->
            distance?.let {
                val km = distance / 1000F
                val totalDistance = round(km * 10F) / 10F
                binding.totalDistance.text = "$totalDistance"+"km"
            }
        })

        viewModel.totalTimeRun.observe(viewLifecycleOwner, { time ->
            time?.let {
                val totalTime = getFormattedStopWatchTime(time, false)
                binding.totalTime.text = totalTime
            }
        })

        viewModel.totalAverageSpeed.observe(viewLifecycleOwner, { avgSpeed ->
            avgSpeed?.let {
                val averageSpeed = round(avgSpeed * 10F) / 10F
                binding.totalAverageTime.text = "$averageSpeed"+"km/hr"
            }
        })

        viewModel.sortRunByDate.observe(viewLifecycleOwner, {
            it?.let {
                val allAvgSpeeds = it.indices.map { i -> BarEntry(i.toFloat(), it[i].averageSpeed) }
                val dataSet = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply {
                    valueTextColor = ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                    color = ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                }
                binding.analyticsChart.data = BarData(dataSet)
                binding.analyticsChart.marker = DashboardMarkerView(it.reversed(), requireContext(), R.layout.dashboard_marker_view)
                binding.analyticsChart.invalidate()
            }
        })

        salutation()
    }

    private fun salutation() {
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

        if (timeOfDay in 0..11) {
            binding.salute.text = "Good Morning"
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            binding.salute.text = "Good Afternoon"
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            binding.salute.text = "Good Evening"
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            binding.salute.text = "Good Night"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(requireContext(), SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(!includeMillis) {
            return "${if(hours < 10) "0" else ""}$hours:" +
                    "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(seconds < 10) "0" else ""}$seconds"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliseconds /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds:" +
                "${if(milliseconds < 10) "0" else ""}$milliseconds"
    }

    override fun onResume() {
        super.onResume()

        observerUserData()
    }

}