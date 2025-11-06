package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.utils.Diagnostics
import com.swizel.android.whereintheworld.utils.RemoteConfig
import com.swizel.android.whereintheworld.utils.RemoteConfigKey
import org.json.JSONObject

class NewGameUseCase(
    private val gameState: GameState,
    private val diagnostics: Diagnostics,
    private val remoteConfig: RemoteConfig,
): SuspendUseCase<NewGameUseCase.Params, Unit>() {

    override suspend fun run(params: Params) {
        diagnostics.trackGameStart(
            gameType = params.gameType,
            gameDifficulty = params.gameDifficulty,
        )

        val config = when(params.gameDifficulty) {
            GameDifficulty.EASY -> {
                remoteConfig.getStringConfig(RemoteConfigKey.EASY_CONFIG)
            }
            GameDifficulty.MEDIUM -> {
                remoteConfig.getStringConfig(RemoteConfigKey.MEDIUM_CONFIG)
            }
            GameDifficulty.HARD -> {
                remoteConfig.getStringConfig(RemoteConfigKey.HARD_CONFIG)
            }
            GameDifficulty.EXTREME -> {
                remoteConfig.getStringConfig(RemoteConfigKey.EXTREME_CONFIG)
            }
        }

        gameState.newGame(
            gameDifficulty = params.gameDifficulty,
            config = JSONObject(config)
        )
    }

    data class Params(
        val gameType: GameType,
        val gameDifficulty: GameDifficulty,
    )
}
