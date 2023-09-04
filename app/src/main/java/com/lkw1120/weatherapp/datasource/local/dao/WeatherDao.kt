package com.lkw1120.weatherapp.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lkw1120.weatherapp.datasource.local.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getWeatherInfo(

    ): List<WeatherEntity>

    @Query("SELECT * FROM weather_table WHERE location_name = :name AND latitude = :lat AND longitude = :lon ORDER BY timestamp DESC LIMIT 1")
    suspend fun getWeatherInfo(
        name: String,
        lat: Double,
        lon: Double
    ): WeatherEntity

    @Upsert
    suspend fun updateWeatherInfo(
        entity: WeatherEntity
    )
}