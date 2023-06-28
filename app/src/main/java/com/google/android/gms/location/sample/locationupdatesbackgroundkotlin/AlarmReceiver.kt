package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.multidex.MultiDexApplication
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

        //val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

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

        notificationDialog()

        val delayMillis : Long = 2000
        AlarmUtils.setAlarm(context, delayMillis)
//        Log.d("AlarmReceiver","Alarm Set date->${sdf.format(date)}")
    }

    private fun notificationDialog() {
        val notificationManager =
            Tracker.tracker?. getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val NOTIFICATION_CHANNEL_ID = "tutorialspoint_01"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_LOW
            )
            // Configure the notification channel.
            notificationChannel.description = "Sample Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(false)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(Tracker?.tracker!!, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setTicker("Tutorialspoint") //.setPriority(Notification.PRIORITY_MAX)
            .setContentTitle("sample notification")
            .setContentText("This is sample notification")
            .setContentInfo("Information")
        notificationManager!!.notify(1, notificationBuilder.build())
    }

}