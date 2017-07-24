package com.fuh.maptest.screen.history

import android.location.Location
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuh.mapstest.R
import com.fuh.maptest.model.Route
import kotlinx.android.synthetic.main.item_history.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lll on 21.07.2017.
 */
class HistoryAdapter(
        items: List<Route> = listOf(),
        val onShowRouteClick: (Route) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var items = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)

        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onShowRouteClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(route: Route, onShowRouteClick: (Route) -> Unit) {
            val dateFormat = SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.US)

            val beginDate = Date(route.beginTime)
            val endDate = Date(route.endTime)

            val beginDateFormatted = dateFormat.format(beginDate)
            val endDateFormatted = dateFormat.format(endDate)

            val elapsedTime = (route.endTime - route.beginTime) / 1000

            val distances = mutableListOf<Float>()
            route.positions!!
                    .fold(route.positions!!.first()) { last, new ->
                        val results = floatArrayOf(0.0f)

                        Location.distanceBetween(last.latitude, last.longitude, new.latitude, new.longitude, results)

                        distances.add(results.first())

                        new
                    }
            val averageDistance = distances.sum()

            val averageSpeed = averageDistance / elapsedTime

            val text = """
                |$beginDateFormatted - $endDateFormatted
                |averageSpeed:$averageSpeed
                |averageTime:$elapsedTime
            """.trimMargin()
            itemView.tvHistoryItemRouteInfo.text = text
            itemView.ivHistoryItemShowRoute.setOnClickListener { onShowRouteClick(route) }
        }
    }
}