package com.swizel.android.whereintheworld.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng

class GameState {

    companion object {
        private val np = LatLng(90.0, 0.0)
        private val sp = LatLng(-90.0, 0.0)
        private val east = LatLng(0.0, -90.0)
        private val west = LatLng(0.0, 90.0)

        // Greatest distance between 2 points seems to be North Pole and South Pole (not East/West).
        // This is the worst possible distance you can be away with a guessed location.
        private var GREATEST_DISTANCE = distanceBetweenPointsInMeters(np, sp)

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

    private var _guesses = mutableListOf<Guess>()
    val guesses: List<Guess> = _guesses
    var numRounds: Int = 0
        private set
    var currentRound: Int = 0
        private set
    var remainingStreetViewTimer: Long = 0L
        private set
    var difficulty: GameDifficulty? = null
        private set

    fun newGame(gameDifficulty: GameDifficulty) {
        _guesses.clear()
        currentRound = -1 // we'll increment this to 0 soon.
        difficulty = gameDifficulty
        numRounds = 3

        // Get ready for the first round.
        prepareNextRound()
    }

    fun setGuessForCurrentRound(location: LatLng?, timeTaken: Long, hint: Hint = Hint.NONE) {
        _guesses[currentRound].guessedLatLng = location
        _guesses[currentRound].guessTime = timeTaken
        _guesses[currentRound].hint = hint
    }

    fun prepareNextRound(): Boolean {
        currentRound++
        remainingStreetViewTimer = 0L
        return if (currentRound < numRounds) {
            _guesses += Guess("", LatLng(1.35, 103.87)) // Singapore
            // There are more rounds to play.
            true
        } else {
            // There are no more rounds
            false
        }
    }

    fun calculateScore(): Long {
        var totalScore = 0f

        _guesses.forEach { guess ->
            guess.guessedLatLng?.let { guessedLatLng ->
                val roundScore =
                    GREATEST_DISTANCE - distanceBetweenPointsInMeters(guess.panoramaLatLng, guessedLatLng)
                // If the player had any hints, then we reduce the score accordingly for that round.
                totalScore += (roundScore * guess.hint.multiplier)
            }
        }

        // Convert score from meters to kilometers.
        return totalScore.toLong() / 1000
    }
}