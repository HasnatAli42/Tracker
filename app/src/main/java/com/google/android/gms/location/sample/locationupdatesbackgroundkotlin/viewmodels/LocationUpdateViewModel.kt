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
package com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.MyCallback
import com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.data.LocationRepository
import java.util.concurrent.Executors

/**
 * Allows Fragment to observer {@link MyLocation} database, follow the state of location updates,
 * and start/stop receiving location updates.
 */
class LocationUpdateViewModel(application: Application){

    private val locationRepository = LocationRepository.getInstance(
        application.applicationContext,
        Executors.newSingleThreadExecutor()
    )

    val receivingLocationUpdates: LiveData<Boolean> = locationRepository.receivingLocationUpdates

    val locationListLiveData = locationRepository.getLocations()

    fun startLocationUpdates(myCallback : MyCallback) = locationRepository.startLocationUpdates(myCallback = myCallback)

    fun stopLocationUpdates() = locationRepository.stopLocationUpdates()
}
