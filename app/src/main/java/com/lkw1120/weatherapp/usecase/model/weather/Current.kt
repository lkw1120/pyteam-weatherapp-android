package com.lkw1120.weatherapp.usecase.model.weather


import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("clouds")
    val clouds: Int? = null,
    @SerializedName("dew_point")
    val dewPoint: Double? = null,
    @SerializedName("dt")
    val dt: Int? = null,
    @SerializedName("feels_like")
    val feelsLike: Double? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("pressure")
    val pressure: Int? = null,
    @SerializedName("sunrise")
    val sunrise: Int? = null,
    @SerializedName("sunset")
    val sunset: Int? = null,
    @SerializedName("temp")
    val temp: Double? = null,
    @SerializedName("uvi")
    val uvi: Double? = null,
    @SerializedName("visibility")
    val visibility: Int? = null,
    @SerializedName("weather")
    val weather: List<Weather?>? = null,
    @SerializedName("wind_deg")
    val windDeg: Int? = null,
    @SerializedName("wind_gust")
    val windGust: Double? = null,
    @SerializedName("wind_speed")
    val windSpeed: Double? = null
)