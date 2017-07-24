package com.fuh.maptest.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

/**
 * Created by lll on 20.07.2017.
 */
open class Route(
        @Required
        @PrimaryKey
        var id: String? = null,
        var beginTime: Long = 0,
        var endTime: Long = 0,
        var positions: RealmList<Position>? = null
) : RealmObject() {
    override fun toString(): String {
        return "Route(beginTime=$beginTime, endTime=$endTime, positions=$positions)"
    }
}