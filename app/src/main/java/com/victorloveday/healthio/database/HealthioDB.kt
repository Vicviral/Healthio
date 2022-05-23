package com.victorloveday.healthio.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.victorloveday.healthio.database.models.Run
import com.victorloveday.healthio.database.models.RunDao
import com.victorloveday.healthio.utils.converters.BitmapConverter

@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(BitmapConverter::class)
abstract class HealthioDB : RoomDatabase() {

    abstract fun getRunDao(): RunDao
}