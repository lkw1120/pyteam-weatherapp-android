package com.lkw1120.weatherapp.datasource

import com.lkw1120.weatherapp.datasource.local.AppDataStore
import com.lkw1120.weatherapp.datasource.local.AppDatabase
import com.lkw1120.weatherapp.datasource.local.entity.LocationEntity
import com.lkw1120.weatherapp.datasource.local.entity.WeatherEntity
import javax.inject.Inject

interface LocalDataSource {

    suspend fun getFirstLoad(): Boolean

    suspend fun setFirstLoad()

    suspend fun getSettings(): Map<String, String>

    suspend fun updateUnits(units: String)

    suspend fun getDatabase(): AppDatabase

    suspend fun getLocationInfo(): LocationEntity

    suspend fun updateLocationInfo(entity: LocationEntity)

    suspend fun getWeatherInfo(): WeatherEntity

    suspend fun updateWeatherInfo(entity: WeatherEntity)
}

class LocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val appDataStore: AppDataStore
) : LocalDataSource {


    override suspend fun getFirstLoad(): Boolean {
        return appDataStore.getFirstLoad()
    }

    override suspend fun setFirstLoad() {
        appDataStore.setFirstLoad()
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

    override suspend fun getLocationInfo(): LocationEntity {
        return appDatabase.locationDao().getLocationInfo()
    }

    override suspend fun updateLocationInfo(entity: LocationEntity) {
        appDatabase.locationDao().updateLocationInfo(entity)
    }

    override suspend fun getWeatherInfo(): WeatherEntity {
        return appDatabase.weatherDao().getWeatherInfo()
    }

    override suspend fun updateWeatherInfo(entity: WeatherEntity) {
        appDatabase.weatherDao().updateWeatherInfo(entity)
    }

}