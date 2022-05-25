package com.victorloveday.healthio.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.victorloveday.healthio.R
import com.victorloveday.healthio.databinding.FragmentTrackingBinding
import com.victorloveday.healthio.services.Polyline
import com.victorloveday.healthio.services.RunTrackingService
import com.victorloveday.healthio.ui.viewmodels.MainViewModel
import com.victorloveday.healthio.utils.RunTrackingUtility
import com.victorloveday.healthio.utils.constants.Constant.MAP_ZOOM
import com.victorloveday.healthio.utils.constants.Constant.PAUSE_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.POLYLINE_COLOR
import com.victorloveday.healthio.utils.constants.Constant.POLYLINE_WIDTH
import com.victorloveday.healthio.utils.constants.Constant.RESUME_OR_START_RUN_SERVICE
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentTrackingBinding

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var map: GoogleMap? = null

    private var currentTimeInMillis = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.startRun.setOnClickListener {
            startRun()
        }

        binding.mapView.getMapAsync {
            map = it
            joinAllPolylines()
        }

        subscribeToObservers()


    }

    private fun sendCommandToRunService(action: String) =
        Intent(requireContext(), RunTrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

   private fun joinLastPolylines() {
       if (pathPoints.isNotEmpty()  && pathPoints.last().size > 1) {
           val preLastCoordinate = pathPoints.last()[pathPoints.last().size - 2]
           val lastCoordinate = pathPoints.last().last()

           val polylineOptions = PolylineOptions()
               .color(POLYLINE_COLOR)
               .width(POLYLINE_WIDTH)
               .add(preLastCoordinate)
               .add(lastCoordinate)

           map?.addPolyline(polylineOptions)
       }
   }
    
    private fun joinAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }


    private fun updateCameraPosition() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            binding.startRunTxt.text = "Start"
            //set finish button visible
            binding.stopRun.visibility = View.VISIBLE
        }else {
            binding.startRunTxt.text = "Stop"
            binding.stopRun.visibility = View.GONE
        }
    }

    private fun startRun() {
        if (isTracking) {
            sendCommandToRunService(PAUSE_RUN_SERVICE)
        }else {
            sendCommandToRunService(RESUME_OR_START_RUN_SERVICE)
        }
    }

    private fun subscribeToObservers() {
        RunTrackingService.isTracking.observe(viewLifecycleOwner, Observer { boolean ->
            updateTracking(boolean)
        })
        RunTrackingService.pathPoints.observe(viewLifecycleOwner, Observer {  polylines ->
            pathPoints = polylines
            joinLastPolylines()
            updateCameraPosition()
        })
        RunTrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTIme = getFormattedStopWatchTime(currentTimeInMillis, true)
            binding.timer.text = formattedTIme
        })
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliSeconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
        milliSeconds -= TimeUnit.HOURS.toMillis((hours))
        val minutes = TimeUnit.MINUTES.toMinutes(milliSeconds)
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MINUTES.toSeconds(milliSeconds)

        if (!includeMillis) {
            return "${if (hours<10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }

        milliSeconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliSeconds /= 10

        return "${if (hours<10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds:" +
                "${if (milliSeconds < 10) "0" else ""}$milliSeconds"

    }

}