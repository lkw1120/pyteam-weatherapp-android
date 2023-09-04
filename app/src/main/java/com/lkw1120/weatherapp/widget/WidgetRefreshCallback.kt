package com.lkw1120.weatherapp.widget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import timber.log.Timber

class WeatherRefreshCallback : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Timber.d("WeatherWorkManager callback action")
        val intent = Intent(context, WeatherWidgetReceiver::class.java).apply {
            action = UPDATE_ACTION
        }
        context.sendBroadcast(intent)
    }

    companion object {
        const val UPDATE_ACTION = "com.lkw1120.weatherapp.widget.action.APPWIDGET_UPDATE"
    }
}