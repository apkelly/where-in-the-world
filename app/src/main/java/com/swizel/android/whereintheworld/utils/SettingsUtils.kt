package com.swizel.android.whereintheworld.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SettingsUtils {

    const val NUM_SOLO_GAMES_PLAYED = "soloGamesPlayed"

    const val NUM_CHALLENGE_GAMES_PLAYED = "challengeGamesPlayed"

    const val NUM_STREETVIEW_CLICKS = "numStreetViewClicks"

    const val FIRST_EVER_GUESS = "firstEverGuess"

    const val FIRST_DATE_PLAYED = "firstDatePlayed"

    const val LAST_DATE_PLAYED = "lastDatePlayed"

    private fun getSharedPreferences(
        context: Context,
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    fun removePreference(
        context: Context?,
        name: String?,
    ) {
        if (context != null && name != null) {
            val editor = getSharedPreferences(context).edit()
            editor.remove(name)
            editor.apply()
        }
    }

    fun getStringPreference(
        context: Context,
        name: String,
        defaultValue: String? = null,
    ): String? = getSharedPreferences(context).getString(name, defaultValue)

    fun getBooleanPreference(
        context: Context,
        name: String,
        defaultValue: Boolean = false,
    ): Boolean = getSharedPreferences(context).getBoolean(name, defaultValue)

    fun getLongPreference(
        context: Context,
        name: String,
        defaultValue: Long,
    ): Long = getSharedPreferences(context).getLong(name, defaultValue)

    fun getLongPreference(
        context: Context,
        name: String,
        defaultValue: Int,
    ): Long = try {
        getSharedPreferences(context).getLong(name, defaultValue.toLong())
    } catch (cce: ClassCastException) {
        getIntPreference(context, name, defaultValue).toLong()
    }

    fun getIntPreference(
        context: Context,
        name: String,
        defaultValue: Int,
    ): Int = getSharedPreferences(context).getInt(name, defaultValue)

    fun getFloatPreference(
        context: Context,
        name: String,
        defaultValue: Float,
    ): Float = getSharedPreferences(context).getFloat(name, defaultValue)

    fun addPreference(
        context: Context,
        name: String,
        value: String,
    ) {
        getSharedPreferences(context)
            .edit()
            .putString(name, value)
            .apply()
    }

    fun addPreference(
        context: Context,
        name: String,
        value: Boolean,
    ) {
        getSharedPreferences(context)
            .edit()
            .putBoolean(name, value)
            .apply()
    }

    fun addPreference(
        context: Context,
        name: String,
        value: Long,
    ) {
        getSharedPreferences(context)
            .edit()
            .putLong(name, value)
            .apply()
    }

    fun addPreference(
        context: Context,
        name: String,
        value: Int,
    ) {
        getSharedPreferences(context)
            .edit()
            .putInt(name, value)
            .apply()
    }

    fun addPreference(
        context: Context,
        name: String,
        value: Float,
    ) {
        getSharedPreferences(context)
            .edit()
            .putFloat(name, value)
            .apply()
    }
}
