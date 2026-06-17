package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.repositories.GameSessionRepository

class GetCurrentGameDifficultyUseCase(
    private val gameSessionRepository: GameSessionRepository,
) : SuspendUseCase<Unit, GameDifficulty>() {

    override suspend fun run(
        params: Unit,
    ): GameDifficulty = gameSessionRepository.difficulty
}
