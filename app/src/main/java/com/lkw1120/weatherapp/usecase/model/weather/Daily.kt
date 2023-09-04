package com.lkw1120.weatherapp.usecase.model.weather


import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("clouds")
    val clouds: Int? = null,
    @SerializedName("dew_point")
    val dewPoint: Double? = null,
    @SerializedName("dt")
    val dt: Int? = null,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("moon_phase")
    val moonPhase: Double? = null,
    @SerializedName("moonrise")
    val moonrise: Int? = null,
    @SerializedName("moonset")
    val moonset: Int? = null,
    @SerializedName("pop")
    val pop: Double? = null,
    @SerializedName("pressure")
    val pressure: Int? = null,
    @SerializedName("rain")
    val rain: Double? = null,
    @SerializedName("summary")
    val summary: String? = null,
    @SerializedName("sunrise")
    val sunrise: Int? = null,
    @SerializedName("sunset")
    val sunset: Int? = null,
    @SerializedName("temp")
    val temp: Temp? = null,
    @SerializedName("uvi")
    val uvi: Double? = null,
    @SerializedName("weather")
    val weather: List<Weather>? = null,
    @SerializedName("wind_deg")
    val windDeg: Int? = null,
    @SerializedName("wind_gust")
    val windGust: Double? = null,
    @SerializedName("wind_speed")
    val windSpeed: Double? = null
)