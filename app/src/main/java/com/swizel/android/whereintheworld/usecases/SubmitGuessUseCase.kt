package com.swizel.android.whereintheworld.usecases

import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import com.swizel.android.whereintheworld.utils.Diagnostics

class SubmitGuessUseCase(
    private val diagnostics: Diagnostics,
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<SubmitGuessUseCase.Params, SubmitGuessUseCase.Result>() {

    override suspend fun run(
        params: Params,
    ): Result {
        val completedRound = gameSessionRepository.currentRound
        val completedRoundData = gameSessionRepository.currentRoundData
        val timeTaken = gameSessionRepository.currentTimeTaken
        val hint = gameSessionRepository.currentHint
        val hasNextRound = gameSessionRepository.submitGuessForCurrentRound(location = params.location)

        diagnostics.trackEvent(
            event = DiagnosticEvent.GUESS_SUBMITTED,
            extras = mapOf(
                "round" to completedRound,
                "difficulty" to gameSessionRepository.difficulty.name,
                "has_guess" to (params.location != null),
                "time_taken_ms" to timeTaken.inWholeMilliseconds,
                "hint" to hint.name,
            ),
        )
        diagnostics.trackEvent(
            event = DiagnosticEvent.ROUND_COMPLETED,
            extras = mapOf(
                "round" to completedRound,
                "difficulty" to gameSessionRepository.difficulty.name,
                "landmark" to completedRoundData.landmark,
                "has_guess" to (params.location != null),
            ),
        )

        return if (hasNextRound) {
            diagnostics.trackEvent(
                event = DiagnosticEvent.ROUND_STARTED,
                extras = mapOf(
                    "round" to gameSessionRepository.currentRound,
                    "difficulty" to gameSessionRepository.difficulty.name,
                ),
            )
            Result.NextRound
        } else {
            Result.GameComplete
        }
    }

    data class Params(
        val location: LatLng?,
    )

    sealed interface Result {
        data object NextRound : Result
        data object GameComplete : Result
    }
}
