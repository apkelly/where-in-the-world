package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import com.swizel.android.whereintheworld.utils.Diagnostics
import com.swizel.android.whereintheworld.utils.RemoteConfig
import org.json.JSONObject

class NewGameUseCase(
    private val gameSessionRepository: GameSessionRepository,
    private val diagnostics: Diagnostics,
    private val remoteConfig: RemoteConfig,
) : SuspendUseCase<NewGameUseCase.Params, Unit>() {

    override suspend fun run(
        params: Params,
    ) {
        diagnostics.trackGameStart(
            gameType = params.gameType,
            gameDifficulty = params.gameDifficulty,
        )

        val config = remoteConfig.getStringConfig(params.gameDifficulty.remoteConfigKey)

        gameSessionRepository.startNewGame(
            gameDifficulty = params.gameDifficulty,
            config = JSONObject(config),
        )

        diagnostics.trackEvent(
            event = DiagnosticEvent.ROUND_STARTED,
            extras = mapOf(
                "round" to gameSessionRepository.currentRound,
                "difficulty" to gameSessionRepository.difficulty.name,
                "game_type" to params.gameType.name,
            ),
        )
    }

    data class Params(
        val gameType: GameType,
        val gameDifficulty: GameDifficulty,
    )
}
