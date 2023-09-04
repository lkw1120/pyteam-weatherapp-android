package com.lkw1120.weatherapp.di

import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lkw1120.weatherapp.datasource.location.LocationTracker
import com.lkw1120.weatherapp.datasource.location.LocationTrackerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTracker {
        return LocationTrackerImpl(
            fusedLocationProviderClient,
            application
        )
    }


    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext appContext: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }
}