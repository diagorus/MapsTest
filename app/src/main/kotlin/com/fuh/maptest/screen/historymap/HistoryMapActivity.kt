package com.fuh.maptest.screen.historymap

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.fuh.mapstest.R
import com.fuh.maptest.model.Position
import com.fuh.maptest.model.Route
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import io.realm.Realm

/**
 * Created by lll on 21.07.2017.
 */
class HistoryMapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_ROUTE_ID = "EXTRA_ROUTE_ID"

        fun newIntent(context: Context, routeId: String): Intent {
            return Intent(context, HistoryMapActivity::class.java).apply {
                putExtra(EXTRA_ROUTE_ID, routeId)
            }
        }
    }

    private lateinit var route: Route

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_map)

        val id = intent.getStringExtra(EXTRA_ROUTE_ID)

        route = Realm.getDefaultInstance().use { realm ->
            val managedRoute = realm.where(Route::class.java).equalTo("id", id).findFirst()

            realm.copyFromRealm(managedRoute)
        }



        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        val latLngs = route.positions!!.map { mapToLatLng(it) }

        map.addPolyline(
                PolylineOptions()
                        .addAll(latLngs)
                        .width(10.0f)
                        .startCap(RoundCap())
                        .endCap(
                                CustomCap(
                                        BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow_drop_up_black_24dp),
                                        10.0f
                                )
                        )
                        .jointType(JointType.ROUND)
                        .color(Color.BLUE)
                        .geodesic(true)
        )

        val cameraBoundsBuilder = LatLngBounds.Builder()
        latLngs.forEach { cameraBoundsBuilder.include(it) }
        val cameraBounds = cameraBoundsBuilder.build()

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(cameraBounds, 300, 300, 0))
    }

    private fun mapToLatLng(position: Position): LatLng {
        return LatLng(position.latitude, position.longitude)
    }
}