package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.navigation.GuessLocationNavKey
import com.swizel.android.whereintheworld.screens.StreetViewUiState
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.utils.Diagnostics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class StreetViewViewModel(
    private val diagnostics: Diagnostics,
    private val gameState: GameState,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StreetViewUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState() {
        val currentRound = gameState.gameRounds[gameState.currentRound]
        _uiState.value = UiState(
            isLoading = LoadingType.NOT_LOADING,
            data = StreetViewUiState(
                numRounds = gameState.numRounds,
                currentRound = gameState.currentRound,
                timeAllowed = 50_000, // This should be based on GameDifficulty or read from Remote Config.
                panoramaLatLng = currentRound.panoramaLatLng,
                landmark = currentRound.landmark,
                country = currentRound.country,
                gameDifficulty = gameState.difficulty,
                currentHint = gameState.currentHint,
            ),
        )
    }

    sealed class Action {
        data class GuessLocation(val timeTaken: Long) : Action()
        data class HintRequested(val hint: Hint) : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
    ) {
        when (action) {
            is Action.GuessLocation -> {
//                diagnostics.trackGameStart(
//                )

                _uiState.value = UiState(
                    isLoading = LoadingType.LOADING,
                )

                gameState.setTimeTakenForCurrentRound(timeTaken = action.timeTaken)

                navigateTo(GuessLocationNavKey)
            }
            is Action.HintRequested -> {
                val currentRound = gameState.gameRounds[gameState.currentRound]
                val scoringHint = if (action.hint.multiplier < gameState.currentHint.multiplier) {
                    action.hint
                } else {
                    gameState.currentHint
                }
                ConsoleLogger.d("Hint Requested : ${action.hint} for ${currentRound.landmark}")
                gameState.setHintForCurrentRound(hint = scoringHint)
                _uiState.value = _uiState.value.copy(
                    data = _uiState.value.data?.copy(currentHint = scoringHint),
                )
            }
        }
    }
}
