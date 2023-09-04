package com.lkw1120.weatherapp.datasource.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume


interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}

class LocationTrackerImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val context: Context
) : LocationTracker {

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    init {
        initLocationClient()
    }

    private fun initLocationClient() {

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            1000L
        ).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Timber.tag(TAG)
                    .d("${result.lastLocation?.latitude} ${result.lastLocation?.longitude}")
                locationClient.removeLocationUpdates(this)
            }
        }
        try {
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Timber.tag(TAG).e("Location Data Update Failed : $e")
        }
    }

    override suspend fun getCurrentLocation(): Location? {

        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission) {
            throw Exception("ExceptionTitles.NO_PERMISSION")
        } else if (!isGpsEnabled) {
            throw Exception("ExceptionTitles.GPS_DISABLED")
        }


        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) cont.resume(result)
                    else {
                        cont.resume(result)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    Timber.tag(TAG).d("locationTracker - success")
                    cont.resume(it)
                }
                addOnFailureListener {
                    Timber.tag(TAG).d("locationTracker - failure")
                    cont.cancel(it.cause)
                }
                addOnCanceledListener {
                    Timber.tag(TAG).d("locationTracker - canceled")
                    cont.cancel()
                }
            }
        }
    }

    companion object {

        private const val TAG = "LocationTracker"
    }
}