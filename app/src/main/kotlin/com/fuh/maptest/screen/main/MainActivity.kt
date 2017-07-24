package com.fuh.maptest.screen.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.fuh.mapstest.R
import com.fuh.maptest.model.Position
import com.fuh.maptest.model.Route
import com.fuh.maptest.screen.history.HistoryActivity
import com.fuh.maptest.util.RxLocation
import io.reactivex.disposables.Disposables
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PERMISSION_FINE_LOCATION = 100
    }

    private var locationDisposable = Disposables.disposed()
    private lateinit var rxLocation: RxLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestLocation()

        rxLocation = RxLocation(this)
        ibMainTrack.setOnClickListener {
            if (locationDisposable.isDisposed) {
                beginTrack()
            } else {
                endTrack()
            }
        }
        btnMainHistory.setOnClickListener {
            val intent = HistoryActivity.newIntent(this)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    }

    private fun requestLocation() {
        if(checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_FINE_LOCATION)
        }
    }

    private fun beginTrack() {
        Timber.i("Track started")
        locationDisposable = rxLocation.observeLocation(1 * 1000, 1.0f)
                .toList()
                .subscribe(
                        { timedLocationsList ->
                            val route = mapToRoute(timedLocationsList)
                            Timber.i("Track ended: $route")

                            Realm.getDefaultInstance().use { realm ->
                                realm.executeTransaction {
                                    realm.insert(mapToRoute(timedLocationsList))
                                }
                            }

                        },
                        {
                            Timber.e(it)
                        }
                )
    }

    private fun endTrack() {
        rxLocation.disable()
        locationDisposable.dispose()

    }

    private fun mapToRoute(locationsList: List<Location>): Route {
        val id = UUID.randomUUID().toString()
        val beginTime = locationsList.first().time
        val endTime = locationsList.last().time

        val positionsList = locationsList.map { Position(it.latitude, it.longitude) }.toTypedArray()
        val positions = RealmList<Position>(*positionsList)

        return Route(
                id,
                beginTime,
                endTime,
                positions
        )
    }
}
