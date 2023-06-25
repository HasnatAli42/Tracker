package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

object AlarmUtils {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setAlarm(context: Context, delayMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent)
    }
}

interface MyCallback {
    fun onSuccess()
    fun onFailure()
}