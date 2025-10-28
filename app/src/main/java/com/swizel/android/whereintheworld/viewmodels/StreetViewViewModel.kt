package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.navigation.GuessLocationNavKey
import com.swizel.android.whereintheworld.screens.StreetViewUiState
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
        _uiState.value = UiState(
            isLoading = LoadingType.NOT_LOADING,
            data = StreetViewUiState(
                numRounds = gameState.numRounds,
                currentRound = gameState.currentRound,
                panoramaLatLng = gameState.guesses.last().panoramaLatLng,
            ),
        )
    }

    sealed class Action {
        data object GuessLocation : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
    ) {
        when (action) {
            is Action.GuessLocation -> {
//                diagnostics.trackGameStart(
//                )
                navigateTo(GuessLocationNavKey)
            }
        }
    }
}
