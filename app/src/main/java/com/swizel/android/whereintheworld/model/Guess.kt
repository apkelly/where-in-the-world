package com.swizel.android.whereintheworld.model

import com.google.android.gms.maps.model.LatLng
import kotlin.time.Duration

data class GameRound(
    val panoramaId: String,
    val panoramaLatLng: LatLng,
    val landmark: String,
    val country: String,
)

data class Guess(
    val guessedLatLng: LatLng,
    val guessTime: Duration,
    val hint: Hint = Hint.NONE,
)

data class GameRoundResult(
    val round: GameRound,
    val guess: Guess?,
)

enum class Hint(val multiplier: Float) {
    NONE(1f),
    COUNTRY(0.5f),
    LANDMARK(0.33f),
}
