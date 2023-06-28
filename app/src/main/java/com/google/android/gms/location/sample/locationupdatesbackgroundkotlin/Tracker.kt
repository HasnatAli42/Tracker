package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.multidex.MultiDexApplication
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.data.MyLocationManager
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.viewmodels.LocationUpdateViewModel
import java.text.SimpleDateFormat
import java.util.*


class Tracker : MultiDexApplication() {

    companion object {
        var tracker: Tracker? = null
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate() {
        super.onCreate()
        Log.d(MyLocationManager::class.simpleName,"App Created")
        var date= Date()
        var sdf= SimpleDateFormat("dd:MM:yyyy hh:mm:ss aa");
        tracker=this

        val locationUpdateViewModel = LocationUpdateViewModel(this);

        locationUpdateViewModel.startLocationUpdates(object : MyCallback {
            override fun onSuccess() {
                val delayMillis : Long = 2000
                AlarmUtils.setAlarm(this@Tracker, delayMillis)
                Log.d("AlarmReceiver","Alarm Set Success date->${sdf.format(date)}")
                // notificationDialog()
//                val notificationManager =
//                    Tracker.tracker?. getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//                notificationManager.cancel(1)

            }

            override fun onFailure() {
                val delayMillis : Long = 2000
                AlarmUtils.setAlarm(this@Tracker, delayMillis)
                Log.d("AlarmReceiver","Alarm Set Failure date->${sdf.format(date)}")
            }
        })


        val delayMillis : Long = 2000
        AlarmUtils.setAlarm(this, delayMillis)
        Log.d("AlarmReceiver","Alarm Set date->${sdf.format(date)}")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(MyLocationManager::class.simpleName,"App Terminate")
    }
}