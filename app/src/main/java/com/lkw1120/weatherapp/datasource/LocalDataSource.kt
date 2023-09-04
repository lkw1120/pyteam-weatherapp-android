package com.lkw1120.weatherapp.datasource

import android.content.Context
import com.lkw1120.weatherapp.datasource.local.AppDataStore
import com.lkw1120.weatherapp.datasource.local.AppDatabase
import com.lkw1120.weatherapp.datasource.local.entity.LocationEntity
import com.lkw1120.weatherapp.datasource.local.entity.WeatherEntity
import javax.inject.Inject

interface LocalDataSource {

    suspend fun getFirstLoad(): Boolean

    suspend fun setFirstLoad()

    suspend fun getLastLocation(): Map<String, Double>

    suspend fun setLastLocation(lat: Double, lon: Double)

    suspend fun getSettings(): Map<String, String>

    suspend fun updateUnits(units: String)

    suspend fun getDatabase(): AppDatabase

    suspend fun getLocationInfo(): List<LocationEntity>

    suspend fun getLocationInfo(name: String, lat: Double, lon: Double): LocationEntity

    suspend fun updateLocationInfo(entity: LocationEntity)

    suspend fun getWeatherInfo(): List<WeatherEntity>

    suspend fun getWeatherInfo(name: String, lat: Double, lon: Double): WeatherEntity

    suspend fun updateWeatherInfo(entity: WeatherEntity)
}

class LocalDataSourceImpl @Inject constructor(
    private val context: Context
) : LocalDataSource {

    private val appDataStore = AppDataStore.getDataStore(context)
    private val appDatabase = AppDatabase.getDatabase(context)


    override suspend fun getFirstLoad(): Boolean {
        return appDataStore.getFirstLoad()
    }

    override suspend fun setFirstLoad() {
        appDataStore.setFirstLoad()
    }

    override suspend fun getLastLocation(): Map<String, Double> {
        return appDataStore.getLastLocation()
    }

    override suspend fun setLastLocation(lat: Double, lon: Double) {
        appDataStore.setLastLocation(lat, lon)
    }

    override suspend fun getSettings(): Map<String, String> {
        return appDataStore.getSettings()
    }

    override suspend fun updateUnits(units: String) {
        return appDataStore.updateUnits(units)
    }

    override suspend fun getDatabase(): AppDatabase {
        return appDatabase
    }

    override suspend fun getLocationInfo(): List<LocationEntity> {
        return appDatabase.locationDao().getLocationInfo()
    }

    override suspend fun getLocationInfo(name: String, lat: Double, lon: Double): LocationEntity {
        return appDatabase.locationDao().getLocationInfo(name, lat, lon)
    }

    override suspend fun updateLocationInfo(entity: LocationEntity) {
        appDatabase.locationDao().updateLocationInfo(entity)
    }

    override suspend fun getWeatherInfo(): List<WeatherEntity> {
        return appDatabase.weatherDao().getWeatherInfo()
    }

    override suspend fun getWeatherInfo(name: String, lat: Double, lon: Double): WeatherEntity {
        return appDatabase.weatherDao().getWeatherInfo(name, lat, lon)
    }

    override suspend fun updateWeatherInfo(entity: WeatherEntity) {
        appDatabase.weatherDao().updateWeatherInfo(entity)
    }

}