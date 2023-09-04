package com.lkw1120.weatherapp.ui.home

import com.lkw1120.weatherapp.usecase.model.geo.LocationInfo
import com.lkw1120.weatherapp.usecase.model.weather.WeatherInfo

sealed interface HomeScreenState {
    object Loading : HomeScreenState

    data class Success(
        val weatherInfo: WeatherInfo?,
        val locationInfo: LocationInfo?,
        val units: String
    ) : HomeScreenState

    data class Error(
        val errorMessage: String?
    ) : HomeScreenState
}