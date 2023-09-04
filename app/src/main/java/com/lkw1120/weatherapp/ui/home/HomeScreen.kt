package com.lkw1120.weatherapp.ui.home

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.gson.JsonParser
import com.lkw1120.weatherapp.R
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.ui.component.CircularProgressBar
import com.lkw1120.weatherapp.ui.location.LocationState
import com.lkw1120.weatherapp.ui.location.LocationViewModel
import com.lkw1120.weatherapp.ui.theme.WeatherAppTheme
import java.util.Locale

@Composable
fun HomeScreen(
    locationViewModel: LocationViewModel,
    homeViewModel: HomeViewModel,
    onNavigateToSettingScreen: () -> Unit
) {

    val activity = (LocalContext.current as? Activity)
    val scope = rememberCoroutineScope()

    val locationState by locationViewModel.latestLocationState.collectAsState()
    val homeScreenState by homeViewModel.homeScreenState.collectAsState()

    when (locationState) {
        is LocationState.Loading -> {
            homeViewModel.refresh()
        }

        is LocationState.Success -> {
            if ((locationState as LocationState.Success).map != null) {
                val map = (locationState as LocationState.Success).map!!
                homeViewModel.getWeatherInfo(map["lat"] as Double, map["lon"] as Double)
            }
        }

        is LocationState.Error -> {

        }
    }



    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            HomeAppBarSection(
                currentState = homeScreenState,
                onNavigateToSettingScreen = onNavigateToSettingScreen
            )
        }
    ) {

        BackgroundImage(homeScreenState)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeContent(homeScreenState) {
                activity?.finish()
            }
        }
    }
}

@Composable
fun BackgroundImage(
    currentState: HomeScreenState
) {
    val backgroundResource = when (currentState) {
        is HomeScreenState.Success -> {
            if (currentState.weatherInfo != null) {
                val weatherInfo = currentState.weatherInfo
                val current = weatherInfo.weatherData?.current!!
                val sunrise = current.sunrise!!
                val sunset = current.sunset!!
                val now = current.dt!!
                if (now in sunrise until sunset) {
                    R.drawable.day_wallpaper
                } else {
                    R.drawable.night_wallpaper
                }
            } else {
                R.drawable.day_wallpaper
            }
        }

        else -> {
            R.drawable.day_wallpaper
        }
    }

    val painter = painterResource(backgroundResource)

    Box(
        modifier = Modifier.fillMaxSize()
        //.background(brush)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

@Composable
fun HomeContent(
    currentState: HomeScreenState,
    errorOnClick: () -> Unit
) {


    when (currentState) {
        is HomeScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment
                    .Center
            ) {
                CircularProgressBar(
                    modifier = Modifier
                        .size(LocalConfiguration.current.screenWidthDp.dp / 3)
                )
            }
        }

        is HomeScreenState.Success -> {
            if (currentState.weatherInfo != null && currentState.locationInfo != null) {

                val context = LocalContext.current
                val weatherInfo = currentState.weatherInfo
                val locationInfo = currentState.locationInfo
                val units = currentState.units


                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        CurrentSection(
                            modifier = Modifier,
                            weatherInfo = weatherInfo,
                            units = units
                        )
                        Box(
                            modifier = Modifier
                                .height(24.dp)
                        )
                        DetailSection(
                            modifier = Modifier,
                            weatherInfo = weatherInfo,
                            units = units
                        )
                    }
                }
            }
        }

        is HomeScreenState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment
                    .Center
            ) {
                CircularProgressBar(
                    modifier = Modifier
                        .size(LocalConfiguration.current.screenWidthDp.dp / 3)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeAppBarSection(
    currentState: HomeScreenState,
    onNavigateToSettingScreen: () -> Unit
) {
    var isNightMode = false
    val title = when (currentState) {
        is HomeScreenState.Success -> {
            if (currentState.weatherInfo != null && currentState.locationInfo != null) {
                val locationInfo = currentState.locationInfo
                val json = JsonParser().parse(locationInfo.raw)
                val location = json.asJsonArray.get(0)?.asJsonObject ?: throw Exception()
                val localNames = location.get("local_names")?.asJsonObject ?: throw Exception()
                val locationName = localNames.get(Locale.getDefault().language)?.asString
                    ?: location.get("name")?.asString ?: AppStrings.unknown

                val weatherInfo = currentState.weatherInfo
                weatherInfo.weatherData?.current?.let {
                    isNightMode = !(it.sunrise!! <= it.dt!! && it.dt < it.sunset!!)
                }
                locationName
            } else {
                AppStrings.unknown
            }
        }

        else -> {
            AppStrings.unknown
        }
    }
    WeatherAppTheme(
        darkTheme = isNightMode
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .statusBarsPadding(),
            colors = TopAppBarDefaults
                .topAppBarColors(containerColor = Color.Transparent),
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            navigationIcon = {

            },
            actions = {
                IconButton(onClick = onNavigateToSettingScreen) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        )
    }
}
