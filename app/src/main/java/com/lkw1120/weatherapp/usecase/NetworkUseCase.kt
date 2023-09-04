package com.lkw1120.weatherapp.usecase

import com.google.gson.GsonBuilder
import com.lkw1120.weatherapp.repository.NetworkRepository
import com.lkw1120.weatherapp.usecase.model.geo.LocationData
import com.lkw1120.weatherapp.usecase.model.geo.LocationInfo
import com.lkw1120.weatherapp.usecase.model.weather.WeatherData
import com.lkw1120.weatherapp.usecase.model.weather.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface NetworkUseCase {

    suspend fun getWeatherInfo(lat: Double, lon: Double, unit: String): Flow<WeatherInfo>

    suspend fun getReverseGeocoding(lat: Double, lon: Double): Flow<LocationInfo>

    suspend fun getGeocoding(location: String): Flow<LocationInfo>

}

class NetworkUseCaseImpl @Inject constructor(
    private val networkRepository: NetworkRepository
) : NetworkUseCase {

    override suspend fun getWeatherInfo(lat: Double, lon: Double, unit: String) =
        networkRepository.getWeatherInfo(lat, lon, unit)
            .map {
                val weatherData = GsonBuilder().serializeNulls().create()
                    .fromJson(it, WeatherData::class.java)
                WeatherInfo(
                    weatherData = weatherData,
                    raw = it
                )
            }

    override suspend fun getReverseGeocoding(lat: Double, lon: Double) =
        networkRepository.getReverseGeocoding(lat, lon)
            .map {
                val locationData = GsonBuilder().serializeNulls().create()
                    .fromJson(it, LocationData::class.java)
                LocationInfo(
                    locationData = locationData,
                    raw = it
                )
            }

    override suspend fun getGeocoding(location: String) =
        networkRepository.getGeocoding(location)
            .map {
                val locationData = GsonBuilder().serializeNulls().create()
                    .fromJson(it, LocationData::class.java)
                LocationInfo(
                    locationData = locationData,
                    raw = it
                )
            }
}