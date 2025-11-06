package com.swizel.android.whereintheworld.model

import com.google.android.gms.maps.model.LatLng

data class GameRound(
    val panoramaId: String,
    val panoramaLatLng: LatLng,
) {
    // Guess can be null if the player runs out of time.
    var guess: Guess? = null
}

data class Guess(
    val guessedLatLng: LatLng,
    val guessTime: Long,
    val hint: Hint = Hint.NONE,
)

enum class Hint(val multiplier: Float) {
    NONE(1f),
    COUNTRY(0.5f),
    LANDMARK(0.33f),
}
