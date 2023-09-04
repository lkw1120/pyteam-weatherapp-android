package com.lkw1120.weatherapp.datasource

import android.content.Context
import com.lkw1120.weatherapp.BuildConfig
import com.lkw1120.weatherapp.datasource.remote.ApiConnection
import com.skydoves.sandwich.ApiResponse
import java.util.Locale
import javax.inject.Inject

interface RemoteDataSource {

    suspend fun getWeatherInfo(lat: Double, lon: Double, unit: String): ApiResponse<String>

    suspend fun getReverseGeocoding(lat: Double, lon: Double): ApiResponse<String>

    suspend fun getGeocoding(location: String): ApiResponse<String>

}

class RemoteDataSourceImpl @Inject constructor(
    private val context: Context
) : RemoteDataSource {

    override suspend fun getWeatherInfo(
        lat: Double,
        lon: Double,
        unit: String
    ): ApiResponse<String> {
        return ApiConnection.getService().getWeatherInfo(
            apiKey = BuildConfig.API_KEY,
            lat = lat,
            lon = lon,
            lang = Locale.getDefault().country,
            units = unit
        )
    }

    override suspend fun getReverseGeocoding(
        lat: Double,
        lon: Double
    ): ApiResponse<String> {
        return ApiConnection.getService().getReverseGeocoding(
            apiKey = BuildConfig.API_KEY,
            lat = lat,
            lon = lon
        )
    }

    override suspend fun getGeocoding(location: String): ApiResponse<String> {
        return ApiConnection.getService().getGeocoding(
            apiKey = BuildConfig.API_KEY,
            location = location
        )
    }

}