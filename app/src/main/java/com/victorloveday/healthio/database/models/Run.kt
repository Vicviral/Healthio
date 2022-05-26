package com.victorloveday.healthio.database.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var img: Bitmap? = null,
    var date: Long = 0L,
    var duration: Long = 0L,
    var burntCalories: Int = 0,
    var distanceCovered: Int = 0,
    var averageSpeed: Float = 0F
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}