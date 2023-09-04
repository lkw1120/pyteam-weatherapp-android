package com.lkw1120.weatherapp.usecase

import android.location.Location
import com.lkw1120.weatherapp.datasource.location.LocationTracker
import javax.inject.Inject

interface LocationUseCase {
    suspend fun getCurrentLocation(): Location?
}

class LocationUseCaseImpl @Inject constructor(
    private val locationTracker: LocationTracker
) : LocationUseCase {

    override suspend fun getCurrentLocation(): Location? =
        locationTracker.getCurrentLocation()

}