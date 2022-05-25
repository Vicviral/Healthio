package com.victorloveday.healthio.utils.constants

import android.graphics.Color

object Constant {
    const val DATABASE_NAME = "healthio_db"
    const val REQUEST_CODE_LOCATION_PERMISSION = 0
    const val RESUME_OR_START_RUN_SERVICE = "RESUME_OR_START_RUN_SERVICE"
    const val PAUSE_RUN_SERVICE = "PAUSE_RUN_SERVICE"
    const val STOP_RUN_SERVICE = "STOP_RUN_SERVICE"
    const val TRACKING_NOTIFICATION_CHANNEL_ID = "TRACKING_CHANNEL"
    const val TRACKING_NOTIFICATION_CHANNEL_NAME = "TRACKING"
    const val NOTIFICATION_ID = 1
    const val SHOW_TRACKING_FRAGMENT = "SHOW_TRACKING_FRAGMENT"
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_UPDATE_INTERVAL = 2000L
    var POLYLINE_COLOR = Color.parseColor("#954FFF")
    const val POLYLINE_WIDTH = 8F
    const val MAP_ZOOM = 15F
    const val RUNNING_TIMER_UPDATE_INTERVAL = 50L
}