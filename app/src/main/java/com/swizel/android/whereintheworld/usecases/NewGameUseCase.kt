package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.utils.Diagnostics
import com.swizel.android.whereintheworld.utils.RemoteConfig
import org.json.JSONObject

class NewGameUseCase(
    private val gameState: GameState,
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

        gameState.newGame(
            gameDifficulty = params.gameDifficulty,
            config = JSONObject(config),
        )
    }

    data class Params(
        val gameType: GameType,
        val gameDifficulty: GameDifficulty,
    )
}
