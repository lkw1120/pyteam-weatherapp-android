package com.lkw1120.weatherapp.ui.location

sealed interface LocationState {
    object Loading : LocationState

    data class Success(
        val map: Map<String, Double>?
    ) : LocationState

    data class Error(
        val errorMessage: String?
    ) : LocationState
}