package com.fuh.maptest.util

import android.content.Context
import android.location.Location
import android.location.LocationManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by lll on 20.07.2017.
 */
class RxLocation(context: Context) {
    private lateinit var locationsSubject: BehaviorSubject<Location>

    private val locationListener = object : BaseLocationListener() {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                locationsSubject.onNext(it)
            }
        }
    }

    private val locationManager: LocationManager by lazy { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    fun isProvided(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun disable() {
        locationManager.removeUpdates(locationListener)
        locationsSubject.onComplete()
    }

    fun observeLocation(minTime: Long, minDistance: Float): Observable<Location> {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener)

        val startLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        locationsSubject =
                startLocation?.let {
                     BehaviorSubject.createDefault(it)
                } ?: BehaviorSubject.create()

        return locationsSubject.hide()
    }
}