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
    con: Context,
    layoutId: Int
) : MarkerView(con, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2F, -height.toFloat())

    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        if (e == null) {
            return
        }

        val currentRunId = e.x.toInt()
        val run = runs[currentRunId]

        var calendar = Calendar.getInstance().apply {
            timeInMillis = run.date
        }

        var dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
//        markerDate = dateFormat.format(calendar.time)

        var avgSpeed = "${run.averageSpeed}km/h"
        markerAvgSpeed.text = avgSpeed

        var distanceInKm = "${run.distanceCovered / 1000f}km"
        markerDistance.text = distanceInKm

        markerDuration.text = HomeFragment().getFormattedStopWatchTime(run.duration)

        var caloriesBurned = "${run.burntCalories}"
        markerCaloriesBurned.text = caloriesBurned

    }

}