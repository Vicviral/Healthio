package com.victorloveday.healthio.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.victorloveday.healthio.R
import com.victorloveday.healthio.databinding.FragmentTrackingBinding
import com.victorloveday.healthio.services.Polyline
import com.victorloveday.healthio.services.RunTrackingService
import com.victorloveday.healthio.ui.viewmodels.MainViewModel
import com.victorloveday.healthio.utils.constants.Constant.POLYLINE_COLOR
import com.victorloveday.healthio.utils.constants.Constant.POLYLINE_WIDTH
import com.victorloveday.healthio.utils.constants.Constant.RESUME_OR_START_RUN_SERVICE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentTrackingBinding

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it
        }

        //test if service works
        binding.startRun.setOnClickListener {
            sendCommandToRunService(RESUME_OR_START_RUN_SERVICE)
        }
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
}