package com.victorloveday.healthio.utils

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.victorloveday.healthio.database.models.Run
import com.victorloveday.healthio.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.dashboard_marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class DashboardMarkerView(
    val runs: List<Run>,
    c: Context,
    layoutId: Int
) : MarkerView(c, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e == null) {
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.date
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        markerDate.text = "date: ${dateFormat.format(calendar.time)}"

        val avgSpeed = "${run.averageSpeed}"
        markerAvgSpeed.text = "km/h: $avgSpeed"

        val distanceInKm = "${run.distanceCovered / 1000f}"
        markerDistance.text = "km: $distanceInKm"

        val duration = HomeFragment().getFormattedStopWatchTime(run.duration)
        markerDuration.text = "time: $duration"

        val caloriesBurned = "${run.burntCalories}"
        markerCaloriesBurned.text = "kcal: $caloriesBurned"
    }
}