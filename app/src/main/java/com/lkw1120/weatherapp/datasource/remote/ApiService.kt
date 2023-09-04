package com.lkw1120.weatherapp.datasource.remote

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/data/3.0/onecall")
    suspend fun getWeatherInfo(
        @Query("appid") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("units") units: String
    ): ApiResponse<String>

    @GET("/geo/1.0/reverse")
    suspend fun getReverseGeocoding(
        @Query("appid") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): ApiResponse<String>

    @GET("/geo/1.0/direct")
    suspend fun getGeocoding(
        @Query("appid") apiKey: String,
        @Query("q") location: String,
        @Query("limit") limit: Int = 10
    ): ApiResponse<String>
}