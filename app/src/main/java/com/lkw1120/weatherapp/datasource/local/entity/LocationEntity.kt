package com.lkw1120.weatherapp.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "location_table"
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("location_name")
    val locationName: String,
    @ColumnInfo("latitude")
    val latitude: Double,
    @ColumnInfo("longitude")
    val longitude: Double,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "timezone")
    val timezone: String,
    @ColumnInfo(name = "location_info")
    val locationInfo: String,
    @ColumnInfo("timestamp")
    val timestamp: Date
)