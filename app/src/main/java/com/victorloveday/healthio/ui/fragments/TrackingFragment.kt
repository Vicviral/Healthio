package com.victorloveday.healthio.ui.fragments

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.victorloveday.healthio.R
import com.victorloveday.healthio.databinding.FragmentTrackingBinding
import com.victorloveday.healthio.services.Polyline
import com.victorloveday.healthio.services.RunTrackingService
import com.victorloveday.healthio.ui.viewmodels.MainViewModel
import com.victorloveday.healthio.utils.constants.Constant.MAP_ZOOM
import com.victorloveday.healthio.utils.constants.Constant.PAUSE_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.POLYLINE_COLOR
import com.victorloveday.healthio.utils.constants.Constant.POLYLINE_WIDTH
import com.victorloveday.healthio.utils.constants.Constant.RESUME_OR_START_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.STOP_RUN_SERVICE
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

    private var menu: Menu? = null

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
        binding = FragmentTrackingBinding.bind(view)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_run_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (currentTimeInMillis > 0L) {
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cancelRun -> {
                showCancelRunDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelRunDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Delete Run?")
            .setMessage("All data for this run event will be lost. Do you want to delete?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Delete") { _, _ ->
                stopRun()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun() {
        sendCommandToRunService(STOP_RUN_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
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
            binding.startRunTxt.text = "Resume"
            //set finish button visible
            binding.stopRun.visibility = View.VISIBLE
        }else {
            binding.startRunTxt.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            binding.stopRun.visibility = View.GONE
        }
    }

    private fun startRun() {
        if (isTracking) {
            menu?.getItem(0)?.isVisible = true
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
}