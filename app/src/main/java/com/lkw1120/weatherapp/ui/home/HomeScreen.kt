package com.lkw1120.weatherapp.ui.home

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.gson.JsonParser
import com.lkw1120.weatherapp.R
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.ui.component.ErrorContent
import com.lkw1120.weatherapp.ui.component.ErrorDialog
import com.lkw1120.weatherapp.ui.location.LocationState
import com.lkw1120.weatherapp.ui.location.LocationViewModel
import com.lkw1120.weatherapp.ui.theme.LightBlue700
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

    LaunchedEffect(locationState) {
        when (locationState) {
            is LocationState.Loading -> {
                homeViewModel.refresh()
            }

            is LocationState.Success -> {
                val map = (locationState as LocationState.Success).map
                if (map != null) {
                    homeViewModel.getWeatherInfo(map["lat"] as Double, map["lon"] as Double)
                }
            }

            is LocationState.Error -> {
                val message = (locationState as LocationState.Error).errorMessage
                if (message != null) {
                    homeViewModel.error(message)
                }
            }
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

        BackgroundImage(
            currentState = homeScreenState,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeContent(
                currentState = homeScreenState,
                onRefreshCallback = { locationViewModel.loadLocation() },
                onErrorCallback = { activity?.finish() },
            )
        }
    }
}

@Composable
fun BackgroundImage(
    currentState: HomeScreenState
) {
    val backgroundResource = when (currentState) {
        is HomeScreenState.Success -> {
            val weatherInfo = currentState.weatherInfo
            if (weatherInfo != null) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeContent(
    currentState: HomeScreenState,
    onRefreshCallback: () -> Unit,
    onErrorCallback: () -> Unit
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isRefreshing by remember { mutableStateOf(true) }
    val refreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            onRefreshCallback()
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        contentAlignment = Alignment.TopCenter
    ) {

        when (currentState) {
            is HomeScreenState.Loading -> {
                isRefreshing = true
            }

            is HomeScreenState.Success -> {
                isRefreshing = false
                val weatherInfo = currentState.weatherInfo
                val locationInfo = currentState.locationInfo
                val units = currentState.units
                if (weatherInfo != null && locationInfo != null) {
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

            is HomeScreenState.Error -> {
                val message = currentState.errorMessage
                if (message != null) {
                    ErrorDialog {
                        ErrorContent(
                            message = message,
                            onRefresh = { onRefreshCallback() },
                            exit = { onErrorCallback() }
                        )
                    }
                }
            }
        }
        PullRefreshIndicator(
            modifier = Modifier,
            refreshing = isRefreshing,
            state = refreshState,
            contentColor = LightBlue700
        )
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
            val locationInfo = currentState.locationInfo
            if (locationInfo != null) {
                val json = JsonParser().parse(locationInfo.raw)
                val location = json.asJsonArray.get(0)?.asJsonObject ?: throw Exception()
                val localNames = location.get("local_names")?.asJsonObject ?: throw Exception()
                val locationName = localNames.get(Locale.getDefault().language)?.asString
                    ?: location.get("name")?.asString ?: AppStrings.unknown

                currentState.weatherInfo?.weatherData?.current?.let {
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
