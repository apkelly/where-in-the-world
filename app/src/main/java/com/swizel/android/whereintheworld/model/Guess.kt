package com.swizel.android.whereintheworld.model

import com.google.android.gms.maps.model.LatLng

data class Guess(
    val panoramaId: String,
    val panoramaLatLng: LatLng,
    var guessedLatLng: LatLng? = null,
    var guessTime: Long? = null,
    var hint: Hint = Hint.NONE
)

enum class Hint(val multiplier: Float) {
    NONE(1f),
    COUNTRY(0.5f),
    LANDMARK(0.33f)
}