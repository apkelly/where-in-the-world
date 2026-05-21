package com.swizel.android.whereintheworld.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import kotlin.random.Random
import org.json.JSONObject

class GameState {

    companion object {
        private val np = LatLng(90.0, 0.0)
        private val sp = LatLng(-90.0, 0.0)

        // Greatest distance between 2 points seems to be North Pole and South Pole (not East/West).
        // This is the worst possible distance you can be away with a guessed location.
        private val GREATEST_DISTANCE = distanceBetweenPointsInMeters(np, sp)

        private fun distanceBetweenPointsInMeters(
            location1: LatLng,
            location2: LatLng,
        ): Float {
            val results = FloatArray(3)
            Location.distanceBetween(
                location1.latitude,
                location1.longitude,
                location2.latitude,
                location2.longitude,
                results,
            )

            return results[0]
        }
    }

    private val _gameRounds = mutableListOf<GameRound>()

    /** Guesses keyed by round index. Null entry means the player ran out of time for that round. */
    private val _guesses = mutableMapOf<Int, Guess>()

    val gameRounds: List<GameRound>
        get() = _gameRounds.toList()

    /** Each GameRound paired with its Guess (null if the player ran out of time). */
    val gameRoundsWithGuesses: List<Pair<GameRound, Guess?>>
        get() = _gameRounds.mapIndexed { index, round -> round to _guesses[index] }

    var numRounds: Int = 0
        private set
    var currentRound: Int = 0
        private set
    var currentTimeTaken: Long = 0
        private set
    var difficulty: GameDifficulty = GameDifficulty.EASY
        private set
    var currentHint: Hint = Hint.NONE
        private set

    fun newGame(
        gameDifficulty: GameDifficulty,
        config: JSONObject,
    ) {
        _gameRounds.clear()
        _guesses.clear()
        currentRound = -1 // we'll increment this to 0 soon.
        difficulty = gameDifficulty
        numRounds = config.getInt("num_rounds")
        val allLocations = config.getJSONArray("locations")

        while (_gameRounds.size < numRounds) {
            val location = allLocations.getJSONObject(Random.nextInt((allLocations.length())))
            val currentLandmarks = gameRounds.map { it.landmark }
            val landmark = location.getString("landmark")
            val country = location.getString("country")
            val latLng = LatLng(location.getDouble("lat"), location.getDouble("lon"))

            // Make sure we don't add duplicate locations to game rounds.
            // TODO: make sure unique number of locations is < numRounds otherwise this loop will never finish.
            if (!currentLandmarks.contains(landmark)) {
                _gameRounds += GameRound(
                    panoramaId = "",
                    panoramaLatLng = latLng,
                    landmark = landmark,
                    country = country,
                )
            }
        }

        ConsoleLogger.d("Game Rounds : ${gameRounds.joinToString(",")}")

        // Get ready for the first round.
        prepareNextRound()
    }

    fun setHintForCurrentRound(
        hint: Hint,
    ) {
        currentHint = hint
    }

    fun setTimeTakenForCurrentRound(
        timeTaken: Long,
    ) {
        currentTimeTaken = timeTaken
    }

    fun setGuessForCurrentRound(
        location: LatLng?,
    ) {
        location?.let {
            _guesses[currentRound] = Guess(location, currentTimeTaken, currentHint)
        }
    }

    fun prepareNextRound(): Boolean {
        currentRound++
        currentHint = Hint.NONE
        currentTimeTaken = 0
        return currentRound < numRounds
    }

    fun calculateScore(): Long {
        var totalScore = 0f

        _gameRounds.forEachIndexed { index, round ->
            _guesses[index]?.let { guess ->
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
