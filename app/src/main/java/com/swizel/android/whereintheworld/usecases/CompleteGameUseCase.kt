package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameRoundResult
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import com.swizel.android.whereintheworld.utils.Diagnostics

class CompleteGameUseCase(
    private val diagnostics: Diagnostics,
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<Unit, CompleteGameUseCase.Result>() {

    override suspend fun run(
        params: Unit,
    ): Result {
        val score = gameSessionRepository.calculateScore()
        val roundResults = gameSessionRepository.gameRoundResults
        val missedRounds = roundResults.count { it.guess == null }
        val hintsUsed = roundResults.count { result -> result.guess?.hint?.multiplier != 1f }

        diagnostics.trackGameEnd()
        diagnostics.trackScore(score = score)
        diagnostics.trackEvent(
            event = DiagnosticEvent.GAME_COMPLETED,
            extras = mapOf(
                "difficulty" to gameSessionRepository.difficulty.name,
                "score" to score,
                "rounds" to roundResults.size,
                "missed_rounds" to missedRounds,
                "hints_used" to hintsUsed,
            ),
        )

        return Result(
            roundResults = roundResults,
            score = score,
        )
    }

    data class Result(
        val roundResults: List<GameRoundResult>,
        val score: Long,
    )
}
