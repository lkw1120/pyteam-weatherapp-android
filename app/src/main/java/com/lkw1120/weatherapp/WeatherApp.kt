package com.lkw1120.weatherapp

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WeatherApp : Application(), Configuration.Provider {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: WeatherApp
        fun AppContext(): Context {
            return instance.applicationContext
        }
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var crashlytics: FirebaseCrashlytics

    override fun onCreate() {
        super.onCreate()
        analytics = Firebase.analytics
        crashlytics = Firebase.crashlytics

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}