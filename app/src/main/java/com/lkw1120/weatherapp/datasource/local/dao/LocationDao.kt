package com.lkw1120.weatherapp.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lkw1120.weatherapp.datasource.local.entity.LocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM location_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLocationInfo(

    ): List<LocationEntity>

    @Query("SELECT * FROM location_table WHERE location_name = :name AND latitude = :lat AND longitude = :lon ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLocationInfo(
        name: String,
        lat: Double,
        lon: Double
    ): LocationEntity

    @Upsert
    suspend fun updateLocationInfo(
        entity: LocationEntity
    )
}