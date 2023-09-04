package com.lkw1120.weatherapp.repository

import com.lkw1120.weatherapp.datasource.LocalDataSource
import com.lkw1120.weatherapp.datasource.local.entity.LocationEntity
import com.lkw1120.weatherapp.datasource.local.entity.WeatherEntity
import javax.inject.Inject

interface DatabaseRepository {

    suspend fun getFirstLoad(): Boolean

    suspend fun setFirstLoad()

    suspend fun getLastLocation(): Map<String, Double>

    suspend fun setLastLocation(lat: Double, lon: Double)

    suspend fun getSettings(): Map<String, String>

    suspend fun updateUnits(units: String)

    suspend fun getLocationInfo(): List<LocationEntity>

    suspend fun getLocationInfo(name: String, lat: Double, lon: Double): LocationEntity

    suspend fun updateLocationInfo(entity: LocationEntity)

    suspend fun getWeatherInfo(): List<WeatherEntity>

    suspend fun getWeatherInfo(name: String, lat: Double, lon: Double): WeatherEntity

    suspend fun updateWeatherInfo(entity: WeatherEntity)
}

class DatabaseRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : DatabaseRepository {

    override suspend fun getFirstLoad(): Boolean {
        return localDataSource.getFirstLoad()
    }

    override suspend fun setFirstLoad() {
        localDataSource.setFirstLoad()
    }

    override suspend fun getLastLocation(): Map<String, Double> {
        return localDataSource.getLastLocation()
    }

    override suspend fun setLastLocation(lat: Double, lon: Double) {
        localDataSource.setLastLocation(lat, lon)
    }

    override suspend fun getSettings(): Map<String, String> {
        return localDataSource.getSettings()
    }

    override suspend fun updateUnits(units: String) {
        return localDataSource.updateUnits(units)
    }

    override suspend fun getLocationInfo(): List<LocationEntity> {
        return localDataSource.getLocationInfo()
    }

    override suspend fun getLocationInfo(name: String, lat: Double, lon: Double): LocationEntity {
        return localDataSource.getLocationInfo(name, lat, lon)
    }

    override suspend fun updateLocationInfo(entity: LocationEntity) {
        localDataSource.updateLocationInfo(entity)
    }

    override suspend fun getWeatherInfo(): List<WeatherEntity> {
        return localDataSource.getWeatherInfo()
    }

    override suspend fun getWeatherInfo(name: String, lat: Double, lon: Double): WeatherEntity {
        return localDataSource.getWeatherInfo(name, lat, lon)
    }

    override suspend fun updateWeatherInfo(entity: WeatherEntity) {
        localDataSource.updateWeatherInfo(entity)
    }

}