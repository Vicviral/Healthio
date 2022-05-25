package com.victorloveday.healthio.services

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.victorloveday.healthio.utils.constants.Constant.PAUSE_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.RESUME_OR_START_RUN_SERVICE
import com.victorloveday.healthio.utils.constants.Constant.STOP_RUN_SERVICE
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
}