package com.lkw1120.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lkw1120.weatherapp.usecase.DatabaseUseCase
import com.lkw1120.weatherapp.usecase.LocationUseCase
import com.lkw1120.weatherapp.usecase.NetworkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseUseCase: DatabaseUseCase,
    private val networkUseCase: NetworkUseCase,
    private val locationUseCase: LocationUseCase
) : ViewModel() {


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
        workerScope.launch(Dispatchers.IO) {
            if (databaseUseCase.getFirstLoad()) {
                databaseUseCase.setFirstLoad()
            }
        }
    }

    fun getWeatherInfo(
        lat: Double, lon: Double
    ) = workerScope.launch(Dispatchers.IO) {
        val units = databaseUseCase.getSettings()["UNITS"].toString()
        networkUseCase.getWeatherInfo(lat, lon, units).collect { weatherInfo ->
            databaseUseCase.updateWeatherInfo(weatherInfo)
            _homeScreenState.value = HomeScreenState.Success(
                weatherInfo = databaseUseCase.getWeatherInfo(),
                locationInfo = databaseUseCase.getLocationInfo(),
                units = units
            )
        }
    }

    fun refresh() = workerScope.launch(Dispatchers.IO) {
        _homeScreenState.value = HomeScreenState.Loading
    }

    fun error(message: String) = workerScope.launch(Dispatchers.IO) {
        throw Exception(message)
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}