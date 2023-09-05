package com.lkw1120.weatherapp.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.lkw1120.weatherapp.service.WeatherService
import com.lkw1120.weatherapp.ui.home.HomeViewModel
import com.lkw1120.weatherapp.ui.location.LocationViewModel
import com.lkw1120.weatherapp.ui.navigation.NavGraph
import com.lkw1120.weatherapp.ui.setting.SettingViewModel
import com.lkw1120.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationViewModel: LocationViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var serviceIntent: Intent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        checkPermission()
        if (serviceIntent == null) {
            serviceIntent = Intent(this, WeatherService::class.java)
            startService(serviceIntent)
        } else {
            serviceIntent = WeatherService().serviceIntent
        }

        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavGraph(
                        locationViewModel = locationViewModel,
                        homeViewModel = homeViewModel,
                        settingViewModel = settingViewModel
                    )
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (serviceIntent != null) {
            stopService(serviceIntent)
            serviceIntent = null
        }
    }

    private fun checkPermission() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            locationViewModel.loadLocation()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )
    }
}