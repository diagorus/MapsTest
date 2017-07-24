package com.fuh.maptest.util

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class RxLatLng : BaseLocationListener() {

    companion object {
        var subLoc: BehaviorSubject<LatLng> = BehaviorSubject.create()
        private var obs = subLoc.hide()
                .filter { it.latitude != 0.0 && it.longitude != 0.0 }
        private var locationManager: LocationManager? = null
        private var locationListener: RxLatLng? = null

        fun launchGps(context: Context) {
//            if(context.checkPermision(Manifest.permission.ACCESS_FINE_LOCATION)){
                locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val crit = Criteria()
                crit.accuracy = Criteria.ACCURACY_FINE
                locationListener = RxLatLng()
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 20f, locationListener)
                val first = (locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER))
                subLoc.onNext(LatLng(first?.latitude ?: 0.0, first?.longitude ?: 0.0))
//            }
        }

        fun observeLocation(): Observable<LatLng> {
            return obs
        }

        fun disable() {
            locationManager?.removeUpdates(locationListener)
        }

        fun isProvided(): Boolean {
            return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
        }

//        fun requestProvided(act: AppCompatActivity, retry: Boolean = false) {
//            if (!isProvided() && !retry) {
//                act.showAlertGps {
//                    if (it) {
//                        act.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ON_GPS)
//                    } else {
//
//                    }
//                }
//            } else if (retry) {
//                act.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ON_GPS)
//            }
//        }
    }

    override fun onLocationChanged(location: Location?) {
        val loc = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        subLoc.onNext(loc)
    }
}