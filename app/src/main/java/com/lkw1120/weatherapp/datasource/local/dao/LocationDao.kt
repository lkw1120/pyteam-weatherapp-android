package com.lkw1120.weatherapp.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lkw1120.weatherapp.datasource.local.entity.LocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM location_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLocationInfo(

    ): LocationEntity

    @Upsert
    suspend fun updateLocationInfo(
        entity: LocationEntity
    )
}