package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository

class GetCurrentRoundProgressUseCase(
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<Unit, GetCurrentRoundProgressUseCase.Result>() {

    override suspend fun run(
        params: Unit,
    ): Result {
        val currentRound = gameSessionRepository.currentRoundData
        return Result(
            numRounds = gameSessionRepository.numRounds,
            currentRound = gameSessionRepository.currentRound,
            landmark = currentRound.landmark,
            country = currentRound.country,
            gameDifficulty = gameSessionRepository.difficulty,
            currentHint = gameSessionRepository.currentHint,
            revealedHints = gameSessionRepository.currentRevealedHints,
        )
    }

    data class Result(
        val numRounds: Int,
        val currentRound: Int,
        val landmark: String,
        val country: String,
        val gameDifficulty: GameDifficulty,
        val currentHint: Hint,
        val revealedHints: List<Hint>,
    )
}
