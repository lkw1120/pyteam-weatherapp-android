package com.lkw1120.weatherapp.worker

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonParser
import com.lkw1120.weatherapp.common.AppStrings
import com.lkw1120.weatherapp.datasource.location.LocationTracker
import com.lkw1120.weatherapp.datasource.location.LocationTrackerImpl
import com.lkw1120.weatherapp.usecase.DatabaseUseCase
import com.lkw1120.weatherapp.usecase.NetworkUseCase
import com.lkw1120.weatherapp.widget.WeatherWidget
import com.lkw1120.weatherapp.widget.WeatherWidgetReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val locationTracker: LocationTracker by lazy {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        LocationTrackerImpl(locationClient, context)
    }

    @Inject
    lateinit var networkUseCase: NetworkUseCase

    @Inject
    lateinit var databaseUseCase: DatabaseUseCase

    override suspend fun doWork(): Result = try {
        Timber.tag(TAG).d("WeatherWidgetWorker init")

        val settings = withContext(Dispatchers.IO) {
            databaseUseCase.getSettings()
        }

        val location = withContext(Dispatchers.IO) {
            locationTracker.getCurrentLocation()
        }

        location?.let {
            val lat = it.latitude
            val lon = it.longitude
            val units = settings["UNITS"] ?: "metric"

            val weatherInfo = withContext(Dispatchers.IO) {
                networkUseCase.getWeatherInfo(lat, lon, units).firstOrNull()
            }
            val locationInfo = withContext(Dispatchers.IO) {
                networkUseCase.getReverseGeocoding(lat, lon).firstOrNull()
            }
            if (weatherInfo != null && locationInfo != null) {
                Timber.tag(TAG).d("weatherInfo : %s", weatherInfo.raw)
                Timber.tag(TAG).d("locationInfo : %s", locationInfo.raw)

                val weatherIcon = weatherInfo.weatherData?.current?.weather?.get(0)?.icon ?: ""
                val weatherTemp =
                    weatherInfo.weatherData?.current?.temp?.toInt()?.toString() ?: "--"

                val temp = weatherInfo.weatherData?.daily?.get(0)?.temp
                val todayMax = temp?.max?.toInt()?.toString() ?: "--"
                val todayMin = temp?.min?.toInt()?.toString() ?: "--"

                val json = JsonParser().parse(locationInfo.raw)
                val location = json.asJsonArray.get(0)?.asJsonObject ?: throw Exception()
                val localNames = location.get("local_names")?.asJsonObject ?: throw Exception()
                val locationName = localNames.get(Locale.getDefault().language)?.asString
                    ?: location.get("name")?.asString ?: AppStrings.unknown

                val glanceId =
                    GlanceAppWidgetManager(context).getGlanceIds(WeatherWidget::class.java)

                glanceId.forEach {
                    updateAppWidgetState(
                        context,
                        PreferencesGlanceStateDefinition,
                        it
                    ) { pref ->
                        pref.toMutablePreferences().apply {
                            this[WeatherWidgetReceiver.icon] = weatherIcon
                            this[WeatherWidgetReceiver.temp] = weatherTemp
                            this[WeatherWidgetReceiver.name] = locationName
                            this[WeatherWidgetReceiver.max] = todayMax
                            this[WeatherWidgetReceiver.min] = todayMin
                        }
                    }
                }
                WeatherWidget().updateAll(context)
            }
        } ?: throw Exception("location not found")
        Result.success()
    } catch (ex: Exception) {
        Timber.tag(TAG).e(ex)
        if (runAttemptCount < 10) Result.retry() else Result.failure()
    }

    companion object {
        private const val TAG = "WeatherWidgetWorker"
    }
}