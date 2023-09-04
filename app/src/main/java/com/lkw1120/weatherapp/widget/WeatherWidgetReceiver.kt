package com.lkw1120.weatherapp.widget

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lkw1120.weatherapp.worker.WeatherWorker
import timber.log.Timber

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = WeatherWidget()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let {
            initWidgetWorker(it)
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        context?.let {
            deleteWidgetWorker(it)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_USER_PRESENT -> {
                Timber.tag(TAG).d("ACTION_USER_PRESENT")
                initWidgetWorker(context)
            }
        }
    }


    private fun initWidgetWorker(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<WeatherWorker>()
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            UNIQUE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )

        val state = workManager.getWorkInfosForUniqueWork(UNIQUE_WORK_NAME).get()
        for (i in state) {
            Timber.d("WeatherWorkManager: $state")
        }
    }

    private fun deleteWidgetWorker(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME)

        val state = workManager.getWorkInfosForUniqueWork(UNIQUE_WORK_NAME).get()
        for (i in state) {
            Timber.d("WeatherWorkManager: $state")
        }
    }

    companion object {
        val icon = stringPreferencesKey("weather_icon")
        val temp = stringPreferencesKey("weather_temp")
        val name = stringPreferencesKey("location_name")
        val max = stringPreferencesKey("today_max")
        val min = stringPreferencesKey("today_min")

        private const val UNIQUE_WORK_NAME = "WeatherWidgetWorker"

        private const val TAG = "WeatherWidgetReceiver"

    }

}