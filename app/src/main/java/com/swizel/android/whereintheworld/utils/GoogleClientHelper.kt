package com.swizel.android.whereintheworld.utils

import android.app.Activity
import android.content.Context
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk

class GoogleClientHelper(
    private val context: Context,
) {

    init {
        PlayGamesSdk.initialize(context)
    }

    fun signIn(
        activity: Activity,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        val gamesSignInClient = PlayGames.getGamesSignInClient(activity)
        gamesSignInClient.signIn()
            .addOnSuccessListener { result ->
                ConsoleLogger.d("signIn success : ${result.isAuthenticated}")
                if (result.isAuthenticated) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                ConsoleLogger.d("signIn failure : ${exception.message}")
                ConsoleLogger.e(exception)

                onFailure()
            }
    }

    fun checkAuthentication(
        activity: Activity,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        PlayGames.getGamesSignInClient(activity)
            .isAuthenticated()
            .addOnSuccessListener { result ->
                ConsoleLogger.d("isAuthenticated success : ${result.isAuthenticated}")
                if (result.isAuthenticated) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                ConsoleLogger.d("isAuthenticated failure : ${exception.message}")
                ConsoleLogger.e(exception)

                onFailure()
            }
    }
}
