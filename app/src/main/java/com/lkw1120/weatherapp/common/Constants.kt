package com.lkw1120.weatherapp.common

import com.lkw1120.weatherapp.R
import com.lkw1120.weatherapp.WeatherApp

object NetworkService {
    const val BASE_URL: String = "https://api.openweathermap.org"
    const val UNIT_METRIC = "metric"
    const val UNIT_IMPERIAL = "imperial"
}

object AppStrings {

    val hourly_forecast = WeatherApp.AppContext().resources.getString(R.string.hourly_forecast)
    val daily_forecast = WeatherApp.AppContext().resources.getString(R.string.daily_forecast)

    val temp = WeatherApp.AppContext().resources.getString(R.string.temp)
    val feels_like = WeatherApp.AppContext().resources.getString(R.string.feels_like)
    val uvi = WeatherApp.AppContext().resources.getString(R.string.uvi)
    val cloudiness = WeatherApp.AppContext().resources.getString(R.string.cloudiness)
    val humidity = WeatherApp.AppContext().resources.getString(R.string.humidity)
    val sunrise = WeatherApp.AppContext().resources.getString(R.string.sunrise)
    val sunset = WeatherApp.AppContext().resources.getString(R.string.sunset)
    val wind_speed = WeatherApp.AppContext().resources.getString(R.string.wind_speed)
    val wind_deg = WeatherApp.AppContext().resources.getString(R.string.wind_deg)
    val visibility = WeatherApp.AppContext().resources.getString(R.string.visibility)
    val pressure = WeatherApp.AppContext().resources.getString(R.string.pressure)
    val degree = WeatherApp.AppContext().resources.getString(R.string.degree)
    val percent = WeatherApp.AppContext().resources.getString(R.string.percent)
    val metric = WeatherApp.AppContext().resources.getString(R.string.metric)
    val imperial = WeatherApp.AppContext().resources.getString(R.string.imperial)
    val meter_per_sec = WeatherApp.AppContext().resources.getString(R.string.meter_per_sec)
    val mile_per_hour = WeatherApp.AppContext().resources.getString(R.string.mile_per_hour)
    val hectopascal = WeatherApp.AppContext().resources.getString(R.string.hectopascal)

    val unknown = WeatherApp.AppContext().resources.getString(R.string.unknown)

    val home_title = WeatherApp.AppContext().resources.getString(R.string.home_title)
    val setting_title = WeatherApp.AppContext().resources.getString(R.string.setting_title)
    val error_title = WeatherApp.AppContext().resources.getString(R.string.error_title)

    val change_unit = WeatherApp.AppContext().resources.getString(R.string.change_unit)
}
