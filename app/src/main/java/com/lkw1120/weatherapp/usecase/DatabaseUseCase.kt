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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface DatabaseUseCase {

    suspend fun getFirstLoad(): Boolean

    suspend fun setFirstLoad()

    suspend fun getSettings(): Map<String, String>

    suspend fun updateUnits(units: String)

    suspend fun getLocationInfo(): LocationInfo

    suspend fun getWeatherInfo(): WeatherInfo

    suspend fun updateLocationInfo(locationInfo: LocationInfo)

    suspend fun updateWeatherInfo(weatherInfo: WeatherInfo)

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

    override suspend fun getSettings(): Map<String, String> {
        return databaseRepository.getSettings()
    }

    override suspend fun updateUnits(units: String) {
        return databaseRepository.updateUnits(units)
    }

    override suspend fun getLocationInfo(): LocationInfo {
        return databaseRepository.getLocationInfo().let { result ->
            val locationData = GsonBuilder().serializeNulls().create()
                .fromJson(result.locationInfo, LocationData::class.java)
            LocationInfo(
                locationData = locationData,
                raw = result.locationInfo
            )
        }
    }

    override suspend fun getWeatherInfo(): WeatherInfo {
        return databaseRepository.getWeatherInfo().let { result ->
            val weatherData = GsonBuilder().serializeNulls().create()
                .fromJson(result.weatherInfo, WeatherData::class.java)
            WeatherInfo(
                weatherData = weatherData,
                raw = result.weatherInfo
            )
        }
    }

    override suspend fun updateLocationInfo(
        locationInfo: LocationInfo
    ) {
        val json = JsonParser().parse(locationInfo.raw)
        val location = json.asJsonArray.get(0)?.asJsonObject ?: throw Exception()
        val localNames = location.get("local_names")?.asJsonObject ?: throw Exception()
        val locationName =
            localNames.get(Locale.getDefault().language)?.asString ?: location.get("name")?.asString
            ?: AppStrings.unknown

        val latitude = locationInfo.locationData?.get(0)?.lat!!
        val longitude = locationInfo.locationData?.get(0)?.lon!!
        val timestamp = Date()
        val country = location?.get("country")?.asString ?: Locale.getDefault().country


        val locationEntity = LocationEntity(
            id = 0,
            locationName = locationName,
            latitude = latitude,
            longitude = longitude,
            country = country,
            locationInfo = Gson().toJson(locationInfo.locationData),
            timestamp = timestamp
        )
        databaseRepository.updateLocationInfo(locationEntity)
    }

    override suspend fun updateWeatherInfo(
        weatherInfo: WeatherInfo
    ) {

        val latitude = weatherInfo.weatherData?.lat!!
        val longitude = weatherInfo.weatherData?.lon!!
        val timestamp = Date()
        val timezone = weatherInfo.weatherData?.timezone!!
        val weatherEntity = WeatherEntity(
            id = 0,
            latitude = latitude,
            longitude = longitude,
            weatherInfo = Gson().toJson(weatherInfo.weatherData),
            timezone = timezone,
            timestamp = timestamp
        )
        databaseRepository.updateWeatherInfo(weatherEntity)
    }

}