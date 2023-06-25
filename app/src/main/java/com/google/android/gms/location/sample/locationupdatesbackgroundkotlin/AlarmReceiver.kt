package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.multidex.MultiDexApplication
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.data.MyLocationManager
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.viewmodels.LocationUpdateViewModel
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    @SuppressLint("SimpleDateFormat", "UnsafeProtectedBroadcastReceiver", "ServiceCast")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context, intent: Intent) {
        val application = context.applicationContext as MultiDexApplication
        val date= Date()
        val sdf= SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");
        Log.d("AlarmReceiver","Alarm Received date->${sdf.format(date)}")

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check if location permission is granted
//        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
//            PackageManager.PERMISSION_GRANTED) {
//            Log.d(MyLocationManager::class.java.simpleName,"Location Permission Granted")
//            // Request location updates
//            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, object : LocationListener {
//                override fun onLocationChanged(location: Location) {
//                    val latitude = location.latitude
//                    val longitude = location.longitude
//
//                    // Handle latitude and longitude values
//                    Toast.makeText(context, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()
//                }}, null)
//        }else {
//            Log.d(MyLocationManager::class.java.simpleName,"Location Permission Not Granted")
//        }

        val locationUpdateViewModel = LocationUpdateViewModel(application);
        locationUpdateViewModel.startLocationUpdates(object : MyCallback {
            override fun onSuccess() {
                val delayMillis : Long = 2000
                AlarmUtils.setAlarm(context, delayMillis)
                Log.d("AlarmReceiver","Alarm Set Success date->${sdf.format(date)}")
            }

            override fun onFailure() {
                val delayMillis : Long = 2000
                AlarmUtils.setAlarm(context, delayMillis)
                Log.d("AlarmReceiver","Alarm Set Failure date->${sdf.format(date)}")
            }
        })
//        val delayMillis : Long = 2000
//        AlarmUtils.setAlarm(context, delayMillis)
//        Log.d("AlarmReceiver","Alarm Set date->${sdf.format(date)}")
    }

}