package com.swizel.android.whereintheworld.viewmodels

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.games.PlayGames
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.navigation.WelcomeNavKey
import com.swizel.android.whereintheworld.screens.GameOverUiState
import com.swizel.android.whereintheworld.utils.Diagnostics
import com.swizel.android.whereintheworld.utils.GoogleClientHelper
import com.swizel.android.whereintheworld.viewmodels.WelcomeViewModel.Action
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class GameOverViewModel(
    private val diagnostics: Diagnostics,
    private val gameState: GameState,
    private val googleClientHelper: GoogleClientHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GameOverUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState(
        activity: Activity,
    ) {
        viewModelScope.launch {
            googleClientHelper.signIn(
                activity = activity,
                onSuccess = {
                    _uiState.value = UiState(
                        isLoading = LoadingType.NOT_LOADING,
                        data = GameOverUiState(
                            gameRounds = gameState.gameRounds,
                            score = gameState.calculateScore(),
                            signedInToGooglePlay = true,
                        ),
                    )
                },
                onFailure = {
                    _uiState.value = UiState(
                        isLoading = LoadingType.NOT_LOADING,
                        data = GameOverUiState(
                            gameRounds = gameState.gameRounds,
                            score = gameState.calculateScore(),
                            signedInToGooglePlay = false,
                        ),
                    )
                },
            )
        }
    }

    sealed class Action {
        data object PlayAgain : Action()
        data class Leaderboards(
            val activity: Activity,
        ) : Action()

        data class Achievements(
            val activity: Activity,
        ) : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
        launchIntent: (Intent) -> Unit,
    ) {
        when (action) {
            is Action.PlayAgain -> {
                // Clear previous game data
                navigateTo(WelcomeNavKey)
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
