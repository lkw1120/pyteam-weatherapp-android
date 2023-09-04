package com.lkw1120.weatherapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.lkw1120.weatherapp.R
import com.lkw1120.weatherapp.ui.MainActivity
import timber.log.Timber


class RestartService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag(TAG).d("onStartCommand")
        val builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle(null)
        builder.setContentText(null)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        builder.setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(
                "default",
                "default_chanel",
                NotificationManager.IMPORTANCE_NONE
            )
        )
        val notification = builder.build()
        startForeground(9, notification)

        startService(Intent(this, WeatherService::class.java))
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "RestartService"
    }

}