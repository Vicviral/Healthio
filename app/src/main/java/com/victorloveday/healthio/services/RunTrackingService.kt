package com.victorloveday.healthio.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleService
import com.victorloveday.healthio.R
import com.victorloveday.healthio.ui.MainActivity
import com.victorloveday.healthio.utils.constants.Constant.NOTIFICATION_ID
import com.victorloveday.healthio.utils.constants.Constant.PAUSE_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.RESUME_OR_START_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.SHOW_TRACKING_FRAGMENT
import com.victorloveday.healthio.utils.constants.Constant.STOP_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.TRACKING_NOTIFICATION_CHANNEL_ID
import com.victorloveday.healthio.utils.constants.Constant.TRACKING_NOTIFICATION_CHANNEL_NAME
import timber.log.Timber

class RunTrackingService: LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                RESUME_OR_START_RUN_SERVICE -> {
                    Timber.d("Started or resume")
                }
                PAUSE_RUN_SERVICE -> {
                    Timber.d("Service paused")
                }
                STOP_RUN_SERVICE -> {
                    Timber.d("Service stopped")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, TRACKING_NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Healthio")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(TRACKING_NOTIFICATION_CHANNEL_ID, TRACKING_NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = SHOW_TRACKING_FRAGMENT
        }, FLAG_UPDATE_CURRENT
    )
//    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(this,
//        0,
//        Intent(this, MainActivity::class.java).also {
//            it.action = SHOW_TRACKING_FRAGMENT
//        }, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//    )
}