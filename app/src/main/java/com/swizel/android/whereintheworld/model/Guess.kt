package com.swizel.android.whereintheworld.model

import com.google.android.gms.maps.model.LatLng

data class Guess(
    val panoramaId: String,
    val panoramaLatLng: LatLng,
    var guessedLatLng: LatLng? = null,
    var guessTime: Long? = null
)