package com.lkw1120.weatherapp.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lkw1120.weatherapp.usecase.DatabaseUseCase
import com.lkw1120.weatherapp.usecase.LocationUseCase
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
class LocationViewModel @Inject constructor(
    private val locationUseCase: LocationUseCase,
    private val databaseUseCase: DatabaseUseCase
) : ViewModel() {

    private val _latestLocationState: MutableStateFlow<LocationState> =
        MutableStateFlow(LocationState.Loading)
    val latestLocationState: StateFlow<LocationState> =
        _latestLocationState.asStateFlow()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Timber.d("에러 발생 : ${throwable.message} in $coroutineContext")
        }
    private val workerScope =
        viewModelScope + coroutineExceptionHandler


    fun loadLocation() = workerScope.launch(Dispatchers.IO) {
        _latestLocationState.value = LocationState.Loading
        val location = locationUseCase.getCurrentLocation()
        location?.let {
            _latestLocationState.emit(
                LocationState.Success(hashMapOf("lat" to it.latitude, "lon" to it.longitude))
            )
        }
    }

    companion object {
        private const val TAG = "LocationViewModel"
    }
}