package com.swizel.android.whereintheworld.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.swizel.android.whereintheworld.model.GameDifficulty


class AnalyticsUtils {

    companion object {

        fun trackButtonClick(context: Context, button: String) {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT, button)

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }

        fun trackLevelStart(context: Context, difficulty: GameDifficulty) {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

            val bundle = Bundle()
            bundle.putSerializable(FirebaseAnalytics.Param.LEVEL, difficulty)

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_START, bundle)
        }

        fun trackLevelEnd(context: Context, difficulty: GameDifficulty) {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

            val bundle = Bundle()
            bundle.putSerializable(FirebaseAnalytics.Param.LEVEL, difficulty)

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_END, bundle)
        }
    }

}

