package com.swizel.android.whereintheworld.utils.impl

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.swizel.android.whereintheworld.extensions.toBundle
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import com.swizel.android.whereintheworld.utils.DiagnosticTrace
import com.swizel.android.whereintheworld.utils.Diagnostics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseDiagnostics(
    private val workDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : Diagnostics {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseCrashlytics: FirebaseCrashlytics
    private lateinit var firebasePerformance: FirebasePerformance

    override suspend fun setup(
        context: Context,
        enabled: Boolean,
    ) {
        withContext(workDispatcher) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            firebaseCrashlytics = FirebaseCrashlytics.getInstance()
            firebasePerformance = FirebasePerformance.getInstance()

            firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
            firebaseCrashlytics.isCrashlyticsCollectionEnabled = enabled
            firebasePerformance.isPerformanceCollectionEnabled = enabled
        }

        if (enabled) {
            ConsoleLogger.i("Diagnostics enabled")
        } else {
            ConsoleLogger.i("Diagnostics disabled")
        }
    }

    override fun trackNavigation(
        route: String,
    ) {
        ConsoleLogger.d("trackNavigation : $route")

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, route)
        }
    }

    override fun trackClick(
        name: String,
    ) {
        ConsoleLogger.d("trackClick : $name")

//        firebaseAnalytics.logEvent("click") {
//            param("label", name)
//        }

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT, name)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    override fun trackEvent(
        event: DiagnosticEvent,
        extras: Map<String, Any>,
    ) {
        ConsoleLogger.d("trackEvent : ${event.name.lowercase()}")

        firebaseAnalytics.logEvent(event.name.lowercase(), extras.toBundle())
    }

    override fun trackException(
        throwable: Throwable,
        extras: Map<String, Any>,
    ) {
        ConsoleLogger.d("trackException : ${throwable.javaClass.simpleName}")

        extras.mapValues {
            it.value.toString()
        }.forEach { entry ->
            firebaseCrashlytics.setCustomKey(entry.key, entry.value)
        }
        firebaseCrashlytics.recordException(throwable)
    }

    override fun trackGameStart(
        gameType: GameType,
        gameDifficulty: GameDifficulty,
    ) {
        val bundle = Bundle()
        bundle.putString("game_type", gameType.name)
        bundle.putInt(FirebaseAnalytics.Param.LEVEL, gameDifficulty.value)
        bundle.putString(FirebaseAnalytics.Param.LEVEL_NAME, gameDifficulty.name)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_START, bundle)
    }

    override fun trackGameEnd() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_END, Bundle())
    }

    override fun trackScore(score: Long) {
        val bundle = Bundle()
        bundle.putSerializable(FirebaseAnalytics.Param.SCORE, score)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, bundle)
    }

    override fun <T> trackPerformance(
        name: DiagnosticTrace,
        f: () -> T,
    ): T {
        ConsoleLogger.d("trackPerformance (${name.trace}) - start")
        val trace = firebasePerformance.newTrace(name.trace)
        return try {
            trace.start()
            f()
        } finally {
            trace.stop()
            ConsoleLogger.d("trackPerformance (${name.trace}) - stop")
        }
    }

    override suspend fun <T> trackSuspendPerformance(
        name: DiagnosticTrace,
        f: suspend () -> T,
    ): T {
        ConsoleLogger.d("trackSuspendPerformance (${name.trace}) - start")
        val trace = firebasePerformance.newTrace(name.trace)
        return try {
            trace.start()
            f()
        } finally {
            trace.stop()
            ConsoleLogger.d("trackSuspendPerformance (${name.trace}) - stop")
        }
    }
}
