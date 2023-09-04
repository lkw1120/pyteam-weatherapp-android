package com.lkw1120.weatherapp.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.lkw1120.weatherapp.ui.MainActivity
import com.lkw1120.weatherapp.widget.WeatherWidgetReceiver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar


class WeatherService : Service() {

    var serviceIntent: Intent? = null

    private val scope = MainScope()

    private val widgetReceiver by lazy {
        WeatherWidgetReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(TAG).d("onStartCommand")
        serviceIntent = intent
        scope.launch {
            registerReceiver(
                widgetReceiver,
                IntentFilter().apply {
                    addAction(Intent.ACTION_USER_PRESENT)
                }
            )
        }
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(widgetReceiver)
        initAlarmManager()
    }

    private fun initAlarmManager() {
        Timber.tag(TAG).d("initAlarmManager")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND, 1)
        val intent = Intent(this, MainActivity::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = sender
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "WeatherService"
    }

}