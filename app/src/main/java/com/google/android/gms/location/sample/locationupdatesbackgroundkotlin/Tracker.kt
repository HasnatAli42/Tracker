package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin

import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.multidex.MultiDexApplication
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.data.MyLocationManager
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.viewmodels.LocationUpdateViewModel
import java.lang.StringBuilder

class Tracker : MultiDexApplication() {

    companion object {
        var tracker: Tracker? = null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(MyLocationManager::class.simpleName,"App Created")
        tracker=this
        val locationUpdateViewModel = LocationUpdateViewModel(tracker!!);

        locationUpdateViewModel.startLocationUpdates()


    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(MyLocationManager::class.simpleName,"App Terminate")
    }
}