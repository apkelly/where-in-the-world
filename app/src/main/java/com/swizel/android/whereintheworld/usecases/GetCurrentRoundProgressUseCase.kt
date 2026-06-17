package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.repositories.GameSessionRepository

class GetCurrentRoundProgressUseCase(
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<Unit, GetCurrentRoundProgressUseCase.Result>() {

    override suspend fun run(
        params: Unit,
    ): Result = Result(
        numRounds = gameSessionRepository.numRounds,
        currentRound = gameSessionRepository.currentRound,
    )

    data class Result(
        val numRounds: Int,
        val currentRound: Int,
    )
}
