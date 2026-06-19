package com.swizel.android.whereintheworld.viewmodels

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.games.PlayGames
import com.swizel.android.whereintheworld.Config
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.navigation.WelcomeNavKey
import com.swizel.android.whereintheworld.screens.GameOverUiState
import com.swizel.android.whereintheworld.usecases.CompleteGameUseCase
import com.swizel.android.whereintheworld.usecases.GetCurrentGameDifficultyUseCase
import com.swizel.android.whereintheworld.utils.Diagnostics
import com.swizel.android.whereintheworld.utils.GoogleClientHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class GameOverViewModel(
    private val diagnostics: Diagnostics,
    private val completeGameUseCase: CompleteGameUseCase,
    private val getCurrentGameDifficultyUseCase: GetCurrentGameDifficultyUseCase,
    private val googleClientHelper: GoogleClientHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GameOverUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()
    private var completionResult: CompleteGameUseCase.Result? = null

    fun fetchUiState(
        activity: Activity,
    ) {
        viewModelScope.launch {
            val completedGame = completionResult ?: completeGameUseCase(Unit).also { completionResult = it }

            googleClientHelper.checkAuthentication(
                activity = activity,
                onSuccess = {
                    updateUiState(completedGame = completedGame, signedInToGooglePlay = true)
                },
                onFailure = {
                    updateUiState(completedGame = completedGame, signedInToGooglePlay = false)
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

        data class SignIn(
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
                viewModelScope.launch {
                    val difficulty = getCurrentGameDifficultyUseCase(Unit)
                    PlayGames.getLeaderboardsClient(action.activity)
                        .getLeaderboardIntent(Config.getLeaderboardId(difficulty))
                        .addOnSuccessListener { intent ->
                            diagnostics.trackNavigation("${difficulty.description} Leaderboard")
                            launchIntent(intent)
                        }
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

            is Action.SignIn -> {
                _uiState.value = _uiState.value.copy(isLoading = LoadingType.LOADING)
                signIn(action.activity)
            }
        }
    }

    private fun signIn(
        activity: Activity,
    ) {
        val completedGame = completionResult ?: return
        viewModelScope.launch {
            googleClientHelper.signIn(
                activity = activity,
                onSuccess = {
                    updateUiState(completedGame = completedGame, signedInToGooglePlay = true)
                },
                onFailure = {
                    updateUiState(completedGame = completedGame, signedInToGooglePlay = false)
                },
            )
        }
    }

    private fun updateUiState(
        completedGame: CompleteGameUseCase.Result,
        signedInToGooglePlay: Boolean,
    ) {
        _uiState.value = UiState(
            isLoading = LoadingType.NOT_LOADING,
            data = GameOverUiState(
                roundResults = completedGame.roundResults,
                score = completedGame.score,
                signedInToGooglePlay = signedInToGooglePlay,
            ),
        )
    }
}
