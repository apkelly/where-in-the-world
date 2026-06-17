package com.swizel.android.whereintheworld.usecases

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameRound
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import com.swizel.android.whereintheworld.utils.DiagnosticTrace
import com.swizel.android.whereintheworld.utils.Diagnostics

internal val sampleRound = GameRound(
    panoramaId = "round-1",
    panoramaLatLng = LatLng(-33.8567844, 151.213108),
    landmark = "Sydney Opera House",
    country = "Australia",
)

internal val secondSampleRound = GameRound(
    panoramaId = "round-2",
    panoramaLatLng = LatLng(48.8583701, 2.2922926),
    landmark = "Eiffel Tower",
    country = "France",
)

internal data class RecordedEvent(
    val event: DiagnosticEvent,
    val extras: Map<String, Any>,
)

internal class RecordingDiagnostics : Diagnostics {
    var gameStart: Pair<GameType, GameDifficulty>? = null
    var gameEndCount: Int = 0
    val trackedScores = mutableListOf<Long>()
    val events = mutableListOf<RecordedEvent>()

    override suspend fun setup(
        context: Context,
        enabled: Boolean,
    ) = Unit

    override fun trackNavigation(route: String) = Unit

    override fun trackClick(name: String) = Unit

    override fun trackEvent(
        event: DiagnosticEvent,
        extras: Map<String, Any>,
    ) {
        events += RecordedEvent(event = event, extras = extras)
    }

    override fun trackException(
        throwable: Throwable,
        extras: Map<String, Any>,
    ) = Unit

    override fun trackGameStart(
        gameType: GameType,
        gameDifficulty: GameDifficulty,
    ) {
        gameStart = gameType to gameDifficulty
    }

    override fun trackGameEnd() {
        gameEndCount += 1
    }

    override fun trackScore(score: Long) {
        trackedScores += score
    }

    override fun <T> trackPerformance(
        name: DiagnosticTrace,
        f: () -> T,
    ): T = f()

    override suspend fun <T> trackSuspendPerformance(
        name: DiagnosticTrace,
        f: suspend () -> T,
    ): T = f()
}
