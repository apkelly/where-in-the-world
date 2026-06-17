package com.swizel.android.whereintheworld.usecases

import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class GetCurrentRoundUseCase(
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<Unit, GetCurrentRoundUseCase.Result>() {

    override suspend fun run(
        params: Unit,
    ): Result {
        val currentRound = gameSessionRepository.currentRoundData
        return Result(
            numRounds = gameSessionRepository.numRounds,
            currentRound = gameSessionRepository.currentRound,
            timeAllowed = 50_000.milliseconds, // This should be based on GameDifficulty or read from Remote Config.
            panoramaLatLng = currentRound.panoramaLatLng,
            landmark = currentRound.landmark,
            country = currentRound.country,
            gameDifficulty = gameSessionRepository.difficulty,
            currentHint = gameSessionRepository.currentHint,
        )
    }

    data class Result(
        val numRounds: Int,
        val currentRound: Int,
        val timeAllowed: Duration,
        val panoramaLatLng: LatLng,
        val landmark: String,
        val country: String,
        val gameDifficulty: GameDifficulty,
        val currentHint: Hint,
    )
}
