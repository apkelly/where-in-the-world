package com.swizel.android.whereintheworld.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.model.Guess
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*
import java.util.concurrent.TimeUnit

class WhereInTheWorldViewModel : ViewModel() {

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

    private var gameDifficulty: GameDifficulty? = null
    private var gameConfig: JSONObject? = null
    var gameType: GameType? = null
    var currentRound: Int = 0
    var remainingStreetViewTimer: Long = 0
    lateinit var guesses: Array<Guess?>

    private var remoteConfig = FirebaseRemoteConfig.getInstance().apply {
        if (BuildConfig.DEBUG) {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(TimeUnit.MINUTES.toSeconds(10))
                    .build()
            )
            setDefaultsAsync(R.xml.local_config)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("Defaults Succeeded : ${getString("easy_config")}")
                    } else {
                        println("Defaults failed")
                    }
                }
        }
    }

    init {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Fetch Succeeded : ${remoteConfig.getString("easy_config")}")
                } else {
                    println("Fetch failed")
                }
            }
    }


    fun startGame(gameDifficulty: GameDifficulty) {
        this.gameDifficulty = gameDifficulty

        val json = when (gameDifficulty) {
            GameDifficulty.EASY -> remoteConfig.getString("easy_config")
            GameDifficulty.MEDIUM -> remoteConfig.getString("medium_config")
            GameDifficulty.HARD -> remoteConfig.getString("hard_config")
            GameDifficulty.EXTREME -> remoteConfig.getString("extreme_config")
        }

        gameConfig = (JSONTokener(json).nextValue() as JSONObject)
        guesses = Array(getNumRounds()) { null }
    }

    fun getGameDifficulty(): GameDifficulty? {
        return gameDifficulty
    }

    fun getRadius(): Int {
        return gameConfig?.getInt("radius_m") ?: throw IllegalStateException("No game config.")
    }

    fun getNumRounds(): Int {
        return gameConfig?.getInt("num_rounds") ?: throw IllegalStateException("No game config.")
    }

    fun getLocation(): LatLng {
        val locations = gameConfig?.getJSONArray("locations")
        locations?.let {
            val index = Random().nextInt(it.length())
            val location = it.getJSONObject(index)
            return LatLng(location.getDouble("lat"), location.getDouble("lon"))
        }

        throw IllegalStateException("Didn't get a location")
    }

    fun calculateScore(): Int {
        var totalScore = 0f

        guesses.filterNotNull().forEach { guess ->
            val roundScore = GREATEST_DISTANCE - distanceBetweenPointsInMeters(guess.panoramaLatLng, guess.guessedLatLng!!)
            // If the player had any hints, then we reduce the score accordingly for that round.
            totalScore += (roundScore * guess.hint.multiplier)
        }

        return totalScore.toInt()
    }

    fun setStreetViewForCurrentRound(panoramaId: String, location: LatLng) {
        guesses[currentRound] = Guess(panoramaId, location)
    }

    fun setGuessForCurrentRound(location: LatLng, timeTaken: Long) {
        guesses[currentRound]?.guessedLatLng = location
        guesses[currentRound]?.guessTime = timeTaken
    }

    fun configureNextRound(): Int {
        // Increment round and reset streetview counter.
        currentRound += 1
        remainingStreetViewTimer = 0

        return currentRound
    }



}