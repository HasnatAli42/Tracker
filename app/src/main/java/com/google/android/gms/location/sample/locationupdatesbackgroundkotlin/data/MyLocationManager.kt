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
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.*
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.data.db.MyLocationEntity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


private const val TAG = "MyLocationManager"

/**
 * Manages all location related tasks for the app.
 */
class MyLocationManager private constructor(private val context: Context) {

    private val _receivingLocationUpdates: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>(false)

    private lateinit var locationManager: LocationManager

//    private val locationRepository = LocationRepository.getInstance(
//        context,
//        Executors.newSingleThreadExecutor()
//    )

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
        interval = 100
        fastestInterval = 50
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        maxWaitTime = 100
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
    fun startLocationUpdates(myCallback: MyCallback) {
        Log.d(TAG, "startLocationUpdates()")

        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d(MyLocationManager::class.java.simpleName, "Location Permission Not Granted")
            return
        }

        try {
            Log.d(TAG, "startLocationUpdates(2)")
            _receivingLocationUpdates.value = true
            // If the PendingIntent is the same as the last request (which it always is), this
            // request will replace any requestLocationUpdates() called before.
            Log.d(TAG, "startLocationUpdates(3)")

//-----------process 4
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5f, object:android.location.LocationListener{
//                override fun onLocationChanged(p0: Location) {
//                    if (p0 == null) {
//                        myCallback.onFailure()
//                        Toast.makeText(
//                            Tracker.tracker!!,
//                            "Cannot get location.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    else {
//                        Log.d(TAG, "startLocationUpdates(7)")
//                        var date = Date()
//                        var sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");
//                        val lat = p0.latitude
//                        val lon = p0.longitude
//
//                        Log.d(
//                            MyLocationManager::class.java.simpleName,
//                            "${lat}-${lon} date->${
//                                sdf.format(date)
//                            }"
//                        )
//
////                        var entity=MyLocationEntity().also {
////                            it.latitude=p0.latitude
////                            it.longitude=p0.longitude
////                            it.date=date
////                        }
////                        locationRepository.addLocation(entity)
//                        myCallback.onSuccess()
//                    }
//
//
//                }
//
//            })
//-----------process 3

//            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener {location->
//                     if (location == null) {
//                        myCallback.onFailure()
//                        Toast.makeText(
//                            Tracker.tracker!!,
//                            "Cannot get location.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    else {
//                        Log.d(TAG, "startLocationUpdates(7)")
//                        var date = Date()
//                        var sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");
//                        val lat = location.latitude
//                        val lon = location.longitude
//
//                        Log.d(
//                            MyLocationManager::class.java.simpleName,
//                            "${lat}-${lon} date->${
//                                sdf.format(date)
//                            }"
//                        )
//                        myCallback.onSuccess()
//                    }
//            }


//-----------process 2


//            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER, object : CancellationToken() {
//                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
//
//                override fun isCancellationRequested() = false
//            })
//                .addOnSuccessListener { location: Location? ->
//                    if (location == null) {
//                        myCallback.onFailure()
//                        Toast.makeText(
//                            Tracker.tracker!!,
//                            "Cannot get location.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    else {
//                        Log.d(TAG, "startLocationUpdates(7)")
//                        var date = Date()
//                        var sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");
//                        val lat = location.latitude
//                        val lon = location.longitude
//
//                        Log.d(
//                            MyLocationManager::class.java.simpleName,
//                            "${lat}-${lon} date->${
//                                sdf.format(date)
//                            }"
//                        )
//                        myCallback.onSuccess()
//                    }
//
//                     }


//----------------------process 1
      //  }
//            var mLocationCallback = object :
//                LocationCallback() {
//
//                @RequiresApi(Build.VERSION_CODES.KITKAT)
//                override fun onLocationResult(p0: LocationResult) {
//                    super.onLocationResult(p0)
//                    _receivingLocationUpdates.value = false
//
//                    Log.d(TAG, "startLocationUpdates(7)")
//                    var date = Date()
//                    var sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");
//
//
//                    if (p0.lastLocation != null) {
//                        Log.d(
//                            MyLocationManager::class.java.simpleName,
//                            "${p0.lastLocation.latitude}-${p0.lastLocation.longitude} date->${
//                                sdf.format(date)
//                            }"
//                        )
//
//                        var location=MyLocationEntity().also {
//                            it.latitude=p0.lastLocation.latitude
//                            it.longitude=p0.lastLocation.longitude
//                            it.date=date
//                        }
//
//                        LocationRepository.getInstance(context, Executors.newSingleThreadExecutor())
//                            .addLocation(location)
//
//                        stopLocationUpdates()
//                        myCallback.onSuccess()
//                    } else {
//                        Log.d(MyLocationManager::class.java.simpleName, "")
//                        myCallback.onFailure()
//                    }
//                }
//
//                override fun onLocationAvailability(p0: LocationAvailability) {
//                    super.onLocationAvailability(p0)
//                }
//            }
//            Log.d(TAG, "startLocationUpdates(4)")
//            fusedLocationClient.requestLocationUpdates(
//                locationRequest,
//                mLocationCallback,
//                Looper.myLooper()!!
//            )
            Log.d(TAG, "startLocationUpdates(5)")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationUpdatePendingIntent)
        } catch (permissionRevoked: SecurityException) {
            _receivingLocationUpdates.value = false
            Log.d(TAG, "startLocationUpdates(6)")
            // Exception only occurs if the user revokes the FINE location permission before
            // requestLocationUpdates() is finished executing (very rare).
            Log.d(TAG, "Location permission revoked; details: $permissionRevoked")
            throw permissionRevoked
        }
    }

    fun stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates()")
        _receivingLocationUpdates.value = false
        fusedLocationClient.removeLocationUpdates(locationUpdatePendingIntent)
    }

    companion object {
        @Volatile
        private var INSTANCE: MyLocationManager? = null

        fun getInstance(context: Context): MyLocationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyLocationManager(context).also { INSTANCE = it }
            }
        }
    }
}
