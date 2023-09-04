package com.lkw1120.weatherapp.usecase

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.datasource.local.entity.LocationEntity
import com.lkw1120.weatherapp.datasource.local.entity.WeatherEntity
import com.lkw1120.weatherapp.repository.DatabaseRepository
import com.lkw1120.weatherapp.usecase.model.geo.LocationData
import com.lkw1120.weatherapp.usecase.model.geo.LocationInfo
import com.lkw1120.weatherapp.usecase.model.weather.WeatherData
import com.lkw1120.weatherapp.usecase.model.weather.WeatherInfo
import timber.log.Timber
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface DatabaseUseCase {

    suspend fun getFirstLoad(): Boolean

    suspend fun setFirstLoad()

    suspend fun getLastLocation(): Map<String, Double>

    suspend fun setLastLocation(lat: Double, lon: Double)

    suspend fun getSettings(): Map<String, String>

    suspend fun updateUnits(units: String)

    suspend fun getLocationInfo(): List<LocationInfo>

    suspend fun getLocationInfo(name: String, lat: Double, lon: Double): LocationInfo

    suspend fun getWeatherInfo(): List<WeatherInfo>

    suspend fun getWeatherInfo(name: String, lat: Double, lon: Double): WeatherInfo

    suspend fun updateWeatherData(weatherInfo: WeatherInfo, locationInfo: LocationInfo)
}

class DatabaseUseCaseImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : DatabaseUseCase {

    override suspend fun getFirstLoad(): Boolean {
        return databaseRepository.getFirstLoad()
    }

    override suspend fun setFirstLoad() {
        databaseRepository.setFirstLoad()
    }

    override suspend fun getLastLocation(): Map<String, Double> {
        return databaseRepository.getLastLocation()
    }

    override suspend fun setLastLocation(lat: Double, lon: Double) {
        databaseRepository.setLastLocation(lat, lon)
    }

    override suspend fun getSettings(): Map<String, String> {
        return databaseRepository.getSettings()
    }

    override suspend fun updateUnits(units: String) {
        return databaseRepository.updateUnits(units)
    }

    override suspend fun getLocationInfo(): List<LocationInfo> {
        return databaseRepository.getLocationInfo()
            .map {
                val locationData = GsonBuilder().serializeNulls().create()
                    .fromJson(it.locationInfo, LocationData::class.java)
                LocationInfo(
                    locationData = locationData,
                    raw = it.locationInfo
                )
            }
    }

    override suspend fun getLocationInfo(name: String, lat: Double, lon: Double): LocationInfo {
        val result = databaseRepository.getLocationInfo(name, lat, lon)
        Timber.d("getLocationInfo : $result")
        val locationData = GsonBuilder().serializeNulls().create()
            .fromJson(result.locationInfo, LocationData::class.java)
        return LocationInfo(
            locationData = locationData,
            raw = result.locationInfo
        )
    }

    override suspend fun getWeatherInfo(): List<WeatherInfo> {
        return databaseRepository.getWeatherInfo()
            .map {
                val weatherData = GsonBuilder().serializeNulls().create()
                    .fromJson(it.weatherInfo, WeatherData::class.java)
                WeatherInfo(
                    weatherData = weatherData,
                    raw = it.weatherInfo
                )
            }
    }

    override suspend fun getWeatherInfo(name: String, lat: Double, lon: Double): WeatherInfo {
        val result = databaseRepository.getWeatherInfo(name, lat, lon)
        Timber.d("getWeatherInfo : $result")
        val weatherData = GsonBuilder().serializeNulls().create()
            .fromJson(result.weatherInfo, WeatherData::class.java)
        return WeatherInfo(
            weatherData = weatherData,
            raw = result.weatherInfo
        )
    }

    override suspend fun updateWeatherData(
        weatherInfo: WeatherInfo,
        locationInfo: LocationInfo
    ) {
        val json = JsonParser().parse(locationInfo.raw)
        val location = json.asJsonArray.get(0)?.asJsonObject ?: throw Exception()
        val localNames = location.get("local_names")?.asJsonObject ?: throw Exception()
        val locationName =
            localNames.get(Locale.getDefault().language)?.asString ?: location.get("name")?.asString
            ?: AppStrings.unknown

        val latitude = weatherInfo.weatherData?.lat!!
        val longitude = weatherInfo.weatherData?.lon!!
        val timestamp = Date()
        val country = location?.get("country")?.asString ?: Locale.getDefault().country
        val timezone = weatherInfo.weatherData?.timezone!!

        val weatherEntity = WeatherEntity(
            id = 0,
            locationName = locationName,
            latitude = latitude,
            longitude = longitude,
            weatherInfo = Gson().toJson(weatherInfo.weatherData),
            timestamp = timestamp
        )
        val locationEntity = LocationEntity(
            id = 0,
            locationName = locationName,
            latitude = latitude,
            longitude = longitude,
            country = country,
            timezone = timezone,
            locationInfo = Gson().toJson(locationInfo.locationData),
            timestamp = timestamp
        )
        databaseRepository.updateWeatherInfo(weatherEntity)
        databaseRepository.updateLocationInfo(locationEntity)
    }

}