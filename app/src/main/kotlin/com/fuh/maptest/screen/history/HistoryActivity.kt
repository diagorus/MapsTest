package com.fuh.maptest.screen.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.fuh.mapstest.R
import com.fuh.maptest.model.Route
import com.fuh.maptest.screen.historymap.HistoryMapActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_history.*

/**
 * Created by lll on 21.07.2017.
 */
class HistoryActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, HistoryActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val routes = Realm.getDefaultInstance().use { realm ->
            val managedRoutes = realm.where(Route::class.java).findAll()

            realm.copyFromRealm(managedRoutes)
        }

        val historyAdapter = HistoryAdapter(routes) {
            val intent = HistoryMapActivity.newIntent(this, it.id!!)

            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        rvHistoryList.adapter = historyAdapter
        rvHistoryList.layoutManager = layoutManager
        rvHistoryList.addItemDecoration(itemDecoration)
    }
}