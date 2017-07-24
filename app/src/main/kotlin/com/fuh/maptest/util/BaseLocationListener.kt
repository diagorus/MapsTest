package com.fuh.maptest.util

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

/**
 * Created by lll on 20.07.2017.
 */
abstract class BaseLocationListener : LocationListener {
    override fun onLocationChanged(location: Location?) {
        //EMPTY IMPLEMENTATION
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //EMPTY IMPLEMENTATION
    }

    override fun onProviderEnabled(provider: String?) {
        //EMPTY IMPLEMENTATION
    }

    override fun onProviderDisabled(provider: String?) {
        //EMPTY IMPLEMENTATION
    }
}