package com.lkw1120.weatherapp.ui.location

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val databaseUseCase: DatabaseUseCase,
    private val networkUseCase: NetworkUseCase,
    private val locationUseCase: LocationUseCase,
) : ViewModel() {

    private val _latestLocationState: MutableStateFlow<LocationState> =
        MutableStateFlow(LocationState.Loading)
    val latestLocationState: StateFlow<LocationState> =
        _latestLocationState.asStateFlow()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Timber.d("에러 발생 : ${throwable.message} in $coroutineContext")
            _latestLocationState.value = LocationState.Error(throwable.message)
        }
    private val workerScope =
        viewModelScope + coroutineExceptionHandler


    fun loadLocation() = workerScope.launch(Dispatchers.IO) {
        _latestLocationState.value = LocationState.Loading
        locationUseCase.getCurrentLocation()?.let { location ->
            networkUseCase.getReverseGeocoding(location.latitude, location.longitude)
                .map { locationInfo ->
                    databaseUseCase.updateLocationInfo(locationInfo)
                }
                .collect {
                    val locationInfo = databaseUseCase.getLocationInfo()
                    val map = mapOf(
                        "lat" to locationInfo.locationData?.get(0)?.lat!!,
                        "lon" to locationInfo.locationData?.get(0)?.lon!!
                    )
                    _latestLocationState.emit(
                        LocationState.Success(map)
                    )
                }
        }
    }

    companion object {
        private const val TAG = "LocationViewModel"
    }
}