package com.lkw1120.weatherapp.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lkw1120.weatherapp.ui.home.HomeScreen
import com.lkw1120.weatherapp.ui.home.HomeViewModel
import com.lkw1120.weatherapp.ui.location.LocationViewModel
import com.lkw1120.weatherapp.ui.setting.SettingViewModel
import com.lkw1120.weatherapp.ui.setting.SettingsScreen

@Composable
fun NavGraph(
    startDestination: String = NavScreen.HomeScreen.route,
    locationViewModel: LocationViewModel,
    homeViewModel: HomeViewModel,
    settingViewModel: SettingViewModel
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier.padding(0.dp),
            navController = navController,
            startDestination = startDestination,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(NavScreen.HomeScreen.route) {
                HomeScreen(
                    locationViewModel = locationViewModel,
                    homeViewModel = homeViewModel,
                    onNavigateToSettingScreen =
                    { navController.navigate(NavScreen.SettingsScreen.route) },
                )
            }
            composable(NavScreen.SettingsScreen.route) {
                SettingsScreen(
                    settingViewModel = settingViewModel
                ) {
                    navController.navigate(NavScreen.HomeScreen.route) {
                        launchSingleTop = true
                        popUpTo(NavScreen.HomeScreen.route)
                    }
                }
            }
        }
    }
}