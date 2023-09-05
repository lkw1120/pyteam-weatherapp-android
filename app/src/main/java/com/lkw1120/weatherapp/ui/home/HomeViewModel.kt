package com.lkw1120.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParser
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.usecase.DatabaseUseCase
import com.lkw1120.weatherapp.usecase.LocationUseCase
import com.lkw1120.weatherapp.usecase.NetworkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseUseCase: DatabaseUseCase,
    private val networkUseCase: NetworkUseCase,
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    private val _settings: MutableStateFlow<Map<String, String>?> =
        MutableStateFlow(null)
    private val settings: Map<String, String>?
        get() = _settings.value

    private val _homeScreenState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState.Loading)
    val homeScreenState: StateFlow<HomeScreenState> =
        _homeScreenState.asStateFlow()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Timber.tag(TAG).e("${throwable.message} in $coroutineContext")
            _homeScreenState.value = HomeScreenState.Error(throwable.message)
        }
    private val workerScope =
        viewModelScope + coroutineExceptionHandler


    init {
        getSettings()
    }

    private fun getSettings() = workerScope.launch(Dispatchers.IO) {
        _settings.value = databaseUseCase.getSettings()
    }

    fun getWeatherInfo(
        lat: Double, lon: Double
    ) = workerScope.launch(Dispatchers.IO) {
        val units = settings?.get("UNITS").toString()
        val weatherInfo = networkUseCase.getWeatherInfo(lat, lon, units).firstOrNull()
        val locationInfo = networkUseCase.getReverseGeocoding(lat, lon).firstOrNull()
        if (weatherInfo != null && locationInfo != null) {
            databaseUseCase.updateWeatherData(weatherInfo, locationInfo)
            val json = JsonParser().parse(locationInfo.raw)
            val location = json.asJsonArray.get(0)?.asJsonObject ?: throw Exception()
            val localNames = location.get("local_names")?.asJsonObject ?: throw Exception()
            val locationName = localNames.get(Locale.getDefault().language)?.asString
                ?: location.get("name")?.asString ?: AppStrings.unknown

            val latitude = (lat * 10000.0).roundToInt() / 10000.0
            val longitude = (lon * 10000.0).roundToInt() / 10000.0

            _homeScreenState.value = HomeScreenState.Success(
                weatherInfo = databaseUseCase.getWeatherInfo(locationName, latitude, longitude),
                locationInfo = databaseUseCase.getLocationInfo(locationName, latitude, longitude),
                units = units
            )
        }
    }

    fun refresh() = workerScope.launch(Dispatchers.IO) {
        _homeScreenState.value = HomeScreenState.Loading
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}