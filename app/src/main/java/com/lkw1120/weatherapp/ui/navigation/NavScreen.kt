package com.lkw1120.weatherapp.ui.navigation

sealed class NavScreen(val route: String) {
    data object HomeScreen : NavScreen(NavRoutes.homeScreen)
    data object SettingsScreen : NavScreen(NavRoutes.settingsScreen)
}
