package com.lkw1120.weatherapp.ui.setting

sealed interface SettingScreenState {
    data object Loading : SettingScreenState

    data class Success(
        val settings: Map<String, String>?
    ) : SettingScreenState

    data class Error(val errorMessage: String?) : SettingScreenState
}