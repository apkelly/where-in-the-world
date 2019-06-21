package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.Config
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType
import java.util.*

class WhereInTheWorldViewModel : ViewModel() {

    var gameDifficulty: GameDifficulty? = null
    var gameType: GameType? = null
    var currentRound: Int = 0
    var remainingStreetViewTimer: Long = 0
    var guesses = Array<LatLng?>(Config.MAX_ROUNDS) { null }

    fun calculateScore(): Int {
        return Random().nextInt(Int.MAX_VALUE)
    }

    fun setGuessForCurrentRound(location: LatLng) {
        guesses[currentRound] = location
    }

    fun configureNextRound(): Int {
        // Increment round and reset streetview counter.
        currentRound += 1
        remainingStreetViewTimer = 0

        return currentRound
    }

}