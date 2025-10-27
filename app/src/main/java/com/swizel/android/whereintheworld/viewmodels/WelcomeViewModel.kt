package com.swizel.android.whereintheworld.viewmodels

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.games.PlayGames
import com.swizel.android.whereintheworld.screens.WelcomeUiState
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.model.GameType
import com.swizel.android.whereintheworld.navigation.GameOverNavKey
import com.swizel.android.whereintheworld.navigation.GuessLocationNavKey
import com.swizel.android.whereintheworld.navigation.StreetViewNavKey
import com.swizel.android.whereintheworld.utils.Diagnostics
import com.swizel.android.whereintheworld.utils.GoogleClientHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class WelcomeViewModel(
    private val googleClientHelper: GoogleClientHelper,
    private val diagnostics: Diagnostics,
    private val gameState: GameState,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<WelcomeUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState(
        activity: Activity,
    ) {
        _uiState.value = _uiState.value.copy(isLoading = LoadingType.LOADING)
        viewModelScope.launch {
            googleClientHelper.signIn(
                activity = activity,
                onSuccess = {
                    _uiState.value = UiState(
                        isLoading = LoadingType.NOT_LOADING,
                        data = WelcomeUiState(
                            signedInToGooglePlay = true
                        )
                    )
                },
                onFailure = {
                    _uiState.value = UiState(
                        isLoading = LoadingType.NOT_LOADING,
                        data = WelcomeUiState(
                            signedInToGooglePlay = false
                        )
                    )
                }
            )
        }
    }

    sealed class Action {
        data class SoloChallenge(
            val gameDifficulty: GameDifficulty
        ) : Action()

        data class QuickChallenge(
            val gameDifficulty: GameDifficulty
        ) : Action()

        data class FriendChallenge(
            val gameDifficulty: GameDifficulty
        ) : Action()

        data class Leaderboards(
            val activity: Activity
        ) : Action()

        data class Achievements(
            val activity: Activity
        ) : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
        launchIntent: (Intent) -> Unit
    ) {
        when (action) {
            is Action.SoloChallenge -> {
                diagnostics.trackGameStart(
                    gameType = GameType.SOLO,
                    gameDifficulty = action.gameDifficulty
                )
                gameState.newGame(action.gameDifficulty)
                navigateTo(StreetViewNavKey)
            }

            is Action.QuickChallenge -> {
                diagnostics.trackGameStart(
                    gameType = GameType.QUICK_CHALLENGE,
                    gameDifficulty = action.gameDifficulty
                )
                gameState.newGame(action.gameDifficulty)
                navigateTo(GuessLocationNavKey)
            }

            is Action.FriendChallenge -> {
                diagnostics.trackGameStart(
                    gameType = GameType.FRIEND_CHALLENGE,
                    gameDifficulty = action.gameDifficulty
                )
                gameState.newGame(action.gameDifficulty)
                navigateTo(GameOverNavKey)
            }

            is Action.Leaderboards -> {
                PlayGames.getLeaderboardsClient(action.activity)
                    .allLeaderboardsIntent
                    .addOnSuccessListener { intent ->
                        diagnostics.trackNavigation("All Leaderboards")
                        launchIntent(intent)
                    }
            }

            is Action.Achievements -> {
                PlayGames.getAchievementsClient(action.activity)
                    .achievementsIntent
                    .addOnSuccessListener { intent ->
                        diagnostics.trackNavigation("Achievements")
                        launchIntent(intent)
                    }
            }
        }
    }
}