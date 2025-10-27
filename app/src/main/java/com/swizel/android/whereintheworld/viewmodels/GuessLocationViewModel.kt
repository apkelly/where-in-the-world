package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.screens.GuessLocationUiState
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.navigation.GameOverNavKey
import com.swizel.android.whereintheworld.navigation.StreetViewNavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class GuessLocationViewModel(
    private val gameState: GameState,
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GuessLocationUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState() {
        _uiState.value = UiState(
            isLoading = LoadingType.NOT_LOADING,
            data = GuessLocationUiState(
                numRounds = gameState.numRounds,
                currentRound = gameState.currentRound
            )
        )
    }

    sealed class Action {
        data class GuessLocation(val location: LatLng?, val timeTaken: Long) : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
    ) {
        when (action) {
            is Action.GuessLocation -> {
                gameState.setGuessForCurrentRound(action.location, action.timeTaken)
                if (gameState.prepareNextRound()) {
                    navigateTo(StreetViewNavKey)
                } else {
                    navigateTo(GameOverNavKey)
                }
            }
        }
    }

}