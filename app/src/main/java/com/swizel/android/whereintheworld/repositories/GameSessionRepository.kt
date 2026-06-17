package com.swizel.android.whereintheworld.repositories

import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameRound
import com.swizel.android.whereintheworld.model.GameRoundResult
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.model.Hint
import kotlin.time.Duration
import org.json.JSONObject

class GameSessionRepository(
    private val gameState: GameState,
) {
    val numRounds: Int
        get() = gameState.numRounds

    val currentRound: Int
        get() = gameState.currentRound

    val currentRoundData: GameRound
        get() = gameState.gameRounds[gameState.currentRound]

    val difficulty: GameDifficulty
        get() = gameState.difficulty

    val currentHint: Hint
        get() = gameState.currentHint

    val currentTimeTaken: Duration
        get() = gameState.currentTimeTaken

    val gameRoundResults: List<GameRoundResult>
        get() = gameState.gameRoundResults

    fun startNewGame(
        gameDifficulty: GameDifficulty,
        config: JSONObject,
    ) {
        gameState.newGame(
            gameDifficulty = gameDifficulty,
            config = config,
        )
    }

    fun recordTimeTakenForCurrentRound(
        timeTaken: Duration,
    ) {
        gameState.setTimeTakenForCurrentRound(timeTaken = timeTaken)
    }

    fun requestHintForCurrentRound(
        hint: Hint,
    ): Hint {
        val scoringHint = if (hint.multiplier < gameState.currentHint.multiplier) {
            hint
        } else {
            gameState.currentHint
        }
        gameState.setHintForCurrentRound(hint = scoringHint)
        return scoringHint
    }

    fun submitGuessForCurrentRound(
        location: LatLng?,
    ): Boolean {
        gameState.setGuessForCurrentRound(location = location)
        return gameState.prepareNextRound()
    }

    fun calculateScore(): Long = gameState.calculateScore()
}
