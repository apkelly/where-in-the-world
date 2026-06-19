package com.swizel.android.whereintheworld.utils

import android.app.Activity
import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.PlayGamesSdk

class GoogleClientHelper(
    context: Context,
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
                ConsoleLogger.d("signIn completed: isAuthenticated=${result.isAuthenticated}")
                if (result.isAuthenticated) {
                    onSuccess()
                } else {
                    ConsoleLogger.w("signIn completed without authentication")
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                logAuthFailure(operation = "signIn", exception = exception)

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
                ConsoleLogger.d("isAuthenticated completed: isAuthenticated=${result.isAuthenticated}")
                if (result.isAuthenticated) {
                    onSuccess()
                } else {
                    ConsoleLogger.w("isAuthenticated completed with false result")
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                logAuthFailure(operation = "isAuthenticated", exception = exception)

                onFailure()
            }
    }

    private fun logAuthFailure(
        operation: String,
        exception: Exception,
    ) {
        val apiException = exception as? ApiException
        if (apiException != null) {
            val code = apiException.statusCode
            val codeString = CommonStatusCodes.getStatusCodeString(code)
            val statusMessage = apiException.status.statusMessage ?: "n/a"
            ConsoleLogger.e(
                "$operation failure: statusCode=$code($codeString), statusMessage=$statusMessage, message=${apiException.message}",
            )
        } else {
            ConsoleLogger.e(
                "$operation failure: exception=${exception::class.java.simpleName}, message=${exception.message}",
            )
        }
        ConsoleLogger.e(exception)
    }
}
