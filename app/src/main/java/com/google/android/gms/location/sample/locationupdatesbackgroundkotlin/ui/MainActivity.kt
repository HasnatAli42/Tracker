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
package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil.setContentView
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.AlarmReceiver
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.R
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.Tracker
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.databinding.ActivityMainBinding
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.viewmodels.LocationUpdateViewModel


/**
 * This app allows a user to receive location updates in the background.
 *
 * Users have four options in Android 11+ regarding location:
 *
 *  * One time only
 *  * Allow while app is in use, i.e., while app is in foreground
 *  * Allow all the time
 *  * Not allow location at all
 *
 * IMPORTANT NOTE: You should generally prefer 'while-in-use' for location updates, i.e., receiving
 * location updates while the app is in use and create a foreground service (tied to a Notification)
 * when the user navigates away from the app. To learn how to do that instead, review the
 * @see <a href="https://codelabs.developers.google.com/codelabs/while-in-use-location/index.html?index=..%2F..index#0">
 * Receive location updates in Android 10 with Kotlin</a> codelab.
 *
 * If you do have an approved use case for receiving location updates in the background, it will
 * require an additional permission (android.permission.ACCESS_BACKGROUND_LOCATION).
 *
 *
 * Best practices require you to spread out your first fine/course request and your background
 * request.
 */
class MainActivity : AppCompatActivity()
{

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView<ActivityMainBinding>(this, R.layout.activity_main)



        if (this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                AlarmReceiver.LOCATION_PERMISSION_REQUEST_CODE
            )
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = packageName
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.d(MainActivity::class.simpleName,"BG Permission Required")
        }else{
            Log.d(MainActivity::class.simpleName,"BG Permission Already")
        }

        val locationUpdateViewModel = LocationUpdateViewModel(Tracker.tracker!!);

        locationUpdateViewModel.locationListLiveData.observeForever {

            if(it.size>0) {
                Log.d(
                    MainActivity::class.simpleName,
                    "data size-> ${it.size}\n Lat=${it.first().latitude} Long=${it.first().longitude}"
                )

                findViewById<TextView>(R.id.records_count).text =
                    "Recorded Point-> ${it.size} \n Lat=${it.first().latitude} Long=${it.first().longitude}"
            }
        }

    }

}
