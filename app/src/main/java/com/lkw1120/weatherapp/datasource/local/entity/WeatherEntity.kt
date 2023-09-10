package com.lkw1120.weatherapp.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "weather_table"
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("latitude")
    val latitude: Double,
    @ColumnInfo("longitude")
    val longitude: Double,
    @ColumnInfo("weather_info")
    val weatherInfo: String,
    @ColumnInfo(name = "timezone")
    val timezone: String,
    @ColumnInfo("timestamp")
    val timestamp: Date
)