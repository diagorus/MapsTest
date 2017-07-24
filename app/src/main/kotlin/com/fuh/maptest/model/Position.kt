package com.fuh.maptest.model

import io.realm.RealmObject

/**
 * Created by lll on 20.07.2017.
 */
open class Position(
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
) : RealmObject()