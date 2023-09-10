package com.lkw1120.weatherapp.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lkw1120.weatherapp.datasource.local.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getWeatherInfo(

    ): WeatherEntity

    @Upsert
    suspend fun updateWeatherInfo(
        entity: WeatherEntity
    )
}