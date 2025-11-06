package com.swizel.android.whereintheworld.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import kotlin.random.Random
import kotlin.uuid.Uuid.Companion.random

class GameState {

    companion object {
        private val np = LatLng(90.0, 0.0)
        private val sp = LatLng(-90.0, 0.0)
        private val east = LatLng(0.0, -90.0)
        private val west = LatLng(0.0, 90.0)

        // Greatest distance between 2 points seems to be North Pole and South Pole (not East/West).
        // This is the worst possible distance you can be away with a guessed location.
        private val GREATEST_DISTANCE = distanceBetweenPointsInMeters(np, sp)

        private fun distanceBetweenPointsInMeters(location1: LatLng, location2: LatLng): Float {
            val results = FloatArray(3)
            Location.distanceBetween(
                location1.latitude,
                location1.longitude,
                location2.latitude,
                location2.longitude,
                results
            )

            return results[0]
        }
    }

    private var _gameRounds = mutableListOf<GameRound>()
    val gameRounds: List<GameRound> = _gameRounds
    var numRounds: Int = 0
        private set
    var currentRound: Int = 0
        private set
    var remainingStreetViewTimer: Long = 0L
        private set
    var difficulty: GameDifficulty? = null
        private set

    fun newGame(
        gameDifficulty: GameDifficulty,
        config: JSONObject,
    ) {
        _gameRounds.clear()
        currentRound = -1 // we'll increment this to 0 soon.
        difficulty = gameDifficulty
        numRounds = config.getInt("num_rounds")
        val allLocations =config.getJSONArray("locations")

        for (i in 0 until numRounds) {
            val location = allLocations.getJSONObject(Random.nextInt((allLocations.length())))
            _gameRounds += GameRound("", LatLng(location.getDouble("lat"), location.getDouble("lon")))
        }

        // Get ready for the first round.
        prepareNextRound()
    }

    fun setGuessForCurrentRound(location: LatLng?, timeTaken: Long, hint: Hint = Hint.NONE) {
        location?.let {
            _gameRounds[currentRound].guess = Guess(location, timeTaken, hint)
        }
    }

    fun prepareNextRound(): Boolean {
        currentRound++
        remainingStreetViewTimer = 0L
        return currentRound < numRounds
    }

    fun calculateScore(): Long {
        var totalScore = 0f

        _gameRounds.forEach { round ->
            round.guess?.let { guess ->
                val roundScore =
                    GREATEST_DISTANCE - distanceBetweenPointsInMeters(round.panoramaLatLng, guess.guessedLatLng)
                // If the player had any hints, then we reduce the score accordingly for that round.
                totalScore += (roundScore * guess.hint.multiplier)
            }
        }

        // Convert score from meters to kilometers.
        return totalScore.toLong() / 1000
    }
}
