package com.swizel.android.whereintheworld.utils

import android.content.Context
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType

interface Diagnostics {
    suspend fun setup(
        context: Context,
        enabled: Boolean,
    )
    fun trackNavigation(
        route: String,
    )
    fun trackClick(
        name: String,
    )
    fun trackEvent(
        event: DiagnosticEvent,
        extras: Map<String, Any> = mapOf(),
    )
    fun trackException(
        throwable: Throwable,
        extras: Map<String, Any> = mapOf(),
    )

    fun trackGameStart(
        gameType: GameType,
        gameDifficulty: GameDifficulty,
    )

    fun trackGameEnd()

    fun trackScore(
        score: Long,
    )

    fun <T> trackPerformance(
        name: DiagnosticTrace,
        f: () -> T,
    ): T
    suspend fun <T> trackSuspendPerformance(
        name: DiagnosticTrace,
        f: suspend () -> T,
    ): T
}

enum class DiagnosticTrace(
    val trace: String,
) {
    DUMMY("dummy"),
}

enum class DiagnosticEvent {
    GAME_STARTED,
    ROUND_STARTED,
    HINT_REQUESTED,
    GUESS_SUBMITTED,
    ROUND_COMPLETED,
    GAME_COMPLETED,
    GOOGLE_PLAY_SIGN_IN_FAILED,
    STREET_VIEW_UNAVAILABLE,
    REMOTE_CONFIG_PARSE_FAILED,
}
