package com.victorloveday.healthio.di

import android.content.Context
import androidx.room.Room
import com.victorloveday.healthio.database.HealthioDB
import com.victorloveday.healthio.utils.constants.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        HealthioDB::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDAO(db: HealthioDB) = db.getRunDao()
}