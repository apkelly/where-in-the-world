package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import com.swizel.android.whereintheworld.utils.Diagnostics

class RequestHintUseCase(
    private val diagnostics: Diagnostics,
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<RequestHintUseCase.Params, Hint>() {

    override suspend fun run(
        params: Params,
    ): Hint {
        val scoringHint = gameSessionRepository.requestHintForCurrentRound(hint = params.hint)
        diagnostics.trackEvent(
            event = DiagnosticEvent.HINT_REQUESTED,
            extras = mapOf(
                "round" to gameSessionRepository.currentRound,
                "difficulty" to gameSessionRepository.difficulty.name,
                "requested_hint" to params.hint.name,
                "scoring_hint" to scoringHint.name,
                "score_multiplier" to scoringHint.multiplier,
            ),
        )
        return scoringHint
    }

    data class Params(
        val hint: Hint,
    )
}
