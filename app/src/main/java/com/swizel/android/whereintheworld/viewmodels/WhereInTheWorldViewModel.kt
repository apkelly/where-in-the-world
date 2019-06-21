package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.Config
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.model.Guess
import java.util.*

class WhereInTheWorldViewModel : ViewModel() {

    var gameDifficulty: GameDifficulty? = null
    var gameType: GameType? = null
    var currentRound: Int = 0
    var remainingStreetViewTimer: Long = 0
    var guesses = Array<Guess?>(Config.MAX_ROUNDS) { null }

    fun calculateScore(): Int {
        return Random().nextInt(Int.MAX_VALUE)
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