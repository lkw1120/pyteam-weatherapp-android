package com.lkw1120.weatherapp.ui.component

import com.lkw1120.weatherapp.R

fun getIconResByIconId(iconId: String) =
    when (iconId) {
        "01d" -> R.drawable.ic_weather_sunny
        "01n" -> R.drawable.ic_weather_night
        "02d" -> R.drawable.ic_weather_cloudy_sunny
        "02n" -> R.drawable.ic_weather_cloudy_night
        "03d" -> R.drawable.ic_weather_cloudy
        "03n" -> R.drawable.ic_weather_cloudy
        "04d" -> R.drawable.ic_weather_windy_sunny
        "04n" -> R.drawable.ic_weather_windy_night
        "09d" -> R.drawable.ic_weather_rainy_sunny
        "09n" -> R.drawable.ic_weather_rainy_night
        "10d" -> R.drawable.ic_weather_rainy
        "10n" -> R.drawable.ic_weather_rainy
        "11d" -> R.drawable.ic_weather_thunder_sunny
        "11n" -> R.drawable.ic_weather_thunder_night
        "13d" -> R.drawable.ic_weather_snow_sunny
        "13n" -> R.drawable.ic_weather_snow_night
        "50d" -> R.drawable.ic_weather_mist
        "50n" -> R.drawable.ic_weather_mist
        else -> R.drawable.ic_weather_sunny
    }