/*
 * Copyright (C) 2020 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.data

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.AlarmUtils
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.LocationUpdatesBroadcastReceiver
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.MyCallback
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.hasPermission
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

private const val TAG = "MyLocationManager"

/**
 * Manages all location related tasks for the app.
 */
class MyLocationManager private constructor(private val context: Context) {

    private val _receivingLocationUpdates: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    /**
     * Status of location updates, i.e., whether the app is actively subscribed to location changes.
     */
    val receivingLocationUpdates: LiveData<Boolean>
        get() = _receivingLocationUpdates

    // The Fused Location Provider provides access to location APIs.
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Stores parameters for requests to the FusedLocationProviderApi.
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        // Sets the desired interval for active location updates. This interval is inexact. You
        // may not receive updates at all if no location sources are available, or you may
        // receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        //
        // IMPORTANT NOTE: Apps running on "O" devices (regardless of targetSdkVersion) may
        // receive updates less frequently than this interval when the app is no longer in the
        // foreground.
        interval = TimeUnit.SECONDS.toMillis(6)

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        fastestInterval = 100

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Creates default PendingIntent for location changes.
     *
     * Note: We use a BroadcastReceiver because on API level 26 and above (Oreo+), Android places
     * limits on Services.
     */
    private val locationUpdatePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    /**
     * Uses the FusedLocationProvider to start location updates if the correct fine locations are
     * approved.
     *
     * @throws SecurityException if ACCESS_FINE_LOCATION permission is removed before the
     * FusedLocationClient's requestLocationUpdates() has been completed.
     *
     */
    @Throws(SecurityException::class)
    fun startLocationUpdates(myCallback : MyCallback) {
        Log.d(TAG, "startLocationUpdates()")

        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d(MyLocationManager::class.java.simpleName,"Location Permission Not Granted")
            return
        }

        try {
            Log.d(TAG, "startLocationUpdates(2)")
            _receivingLocationUpdates.value = true
            // If the PendingIntent is the same as the last request (which it always is), this
            // request will replace any requestLocationUpdates() called before.
            Log.d(TAG, "startLocationUpdates(3)")
            var mLocationCallback = object :
                LocationCallback() {

                @RequiresApi(Build.VERSION_CODES.KITKAT)
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    Log.d(TAG, "startLocationUpdates(7)")
                    var date=Date()
                    var sdf=SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");


                    if (p0.lastLocation != null) {
                        Log.d(MyLocationManager::class.java.simpleName,"${p0.lastLocation.latitude}-${p0.lastLocation.longitude} date->${sdf.format(date)}")
                        myCallback.onSuccess()
                    }else{
                        Log.d(MyLocationManager::class.java.simpleName,"")
                        myCallback.onFailure()
                    }
                }

                override fun onLocationAvailability(p0: LocationAvailability) {
                    super.onLocationAvailability(p0)
                }
            }
            Log.d(TAG, "startLocationUpdates(4)")
            fusedLocationClient.requestLocationUpdates(locationRequest,mLocationCallback, Looper.myLooper()!!)
            Log.d(TAG, "startLocationUpdates(5)")
        } catch (permissionRevoked: SecurityException) {
            _receivingLocationUpdates.value = false
            Log.d(TAG, "startLocationUpdates(6)")
            // Exception only occurs if the user revokes the FINE location permission before
            // requestLocationUpdates() is finished executing (very rare).
            Log.d(TAG, "Location permission revoked; details: $permissionRevoked")
            throw permissionRevoked
        }
    }

    @MainThread
    fun stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates()")
        _receivingLocationUpdates.value = false
        fusedLocationClient.removeLocationUpdates(locationUpdatePendingIntent)
    }

    companion object {
        @Volatile private var INSTANCE: MyLocationManager? = null

        fun getInstance(context: Context): MyLocationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyLocationManager(context).also { INSTANCE = it }
            }
        }
    }
}
