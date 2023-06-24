package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.data.MyLocationManager
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.viewmodels.LocationUpdateViewModel
import java.lang.StringBuilder


open class OnBootReceiver : BroadcastReceiver() {

    //private val locationUpdateViewModel = LocationUpdateViewModel(Tracker.tracker!!);

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(MyLocationManager::class.java.simpleName, "Booting..")
        //locationUpdateViewModel.startLocationUpdates()

//        locationUpdateViewModel.locationListLiveData
//            .observeForever {locations->
//
//                locations?.let {
//                    //Log.d(com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.ui.TAG, "Got ${locations.size} locations")
//
//                    if (locations.isEmpty()) {
//                        Log.d(MyLocationManager::class.simpleName,"Empty Location")
//                    } else {
//                        val outputStringBuilder = StringBuilder("")
//                        for (location in locations) {
//                            outputStringBuilder.append(location.toString() + "\n")
//                        }
//                        Log.d(MyLocationManager::class.simpleName,outputStringBuilder.toString())
//
//                        //    binding.locationOutputTextView.text = outputStringBuilder.toString()
//                    }
//                }
//
//            }

    }
}
