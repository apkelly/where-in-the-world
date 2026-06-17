package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import kotlin.time.Duration

class RecordStreetViewTimeUseCase(
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<RecordStreetViewTimeUseCase.Params, Unit>() {

    override suspend fun run(
        params: Params,
    ) {
        gameSessionRepository.recordTimeTakenForCurrentRound(timeTaken = params.timeTaken)
    }

    data class Params(
        val timeTaken: Duration,
    )
}
