package com.lkw1120.weatherapp.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lkw1120.weatherapp.common.NetworkService.UNIT_IMPERIAL
import com.lkw1120.weatherapp.common.NetworkService.UNIT_METRIC
import com.lkw1120.weatherapp.usecase.DatabaseUseCase
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
class SettingViewModel @Inject constructor(
    private val databaseUseCase: DatabaseUseCase,
    private val networkUseCase: NetworkUseCase
) : ViewModel() {

    private val _settingScreenState: MutableStateFlow<SettingScreenState> =
        MutableStateFlow(SettingScreenState.Loading)
    val settingScreenState: StateFlow<SettingScreenState> =
        _settingScreenState.asStateFlow()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Timber.tag(TAG).e("${throwable.message} in $coroutineContext")
            _settingScreenState.value = SettingScreenState.Error(throwable.message)
        }

    private val workerScope =
        viewModelScope + coroutineExceptionHandler

    init {
        workerScope.launch(Dispatchers.IO) {
            _settingScreenState.value =
                SettingScreenState.Success(databaseUseCase.getSettings())
        }
    }

    fun updateUnits(units: String) = workerScope.launch(Dispatchers.IO) {
        _settingScreenState.value =
            SettingScreenState.Loading
        if (units == UNIT_METRIC) {
            databaseUseCase.updateUnits(UNIT_IMPERIAL)
        } else {
            databaseUseCase.updateUnits(UNIT_METRIC)
        }
        _settingScreenState.value =
            SettingScreenState.Success(databaseUseCase.getSettings())
    }


    companion object {
        private const val TAG = "SettingViewModel"
    }
}