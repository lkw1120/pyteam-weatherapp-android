package com.lkw1120.weatherapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber


class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.tag(TAG).d("onReceive")
        context?.startForegroundService(Intent(context, RestartService::class.java))
    }

    companion object {
        private const val TAG = "RebootReceiver"
    }
}