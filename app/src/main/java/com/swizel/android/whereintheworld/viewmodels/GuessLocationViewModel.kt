package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.screens.GuessLocationUiState
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.screens.StreetViewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class GuessLocationViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GuessLocationUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState() {
        _uiState.value = UiState(
            isLoading = LoadingType.NOT_LOADING,
            data = GuessLocationUiState(
                dummy = "dummy"
            )
        )
    }

    sealed class Action {
        data class GuessLocation(val location: LatLng, val timeTaken: Long) : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
    ) {
        when (action) {
            is Action.GuessLocation -> {

//        guesses[currentRound]?.guessedLatLng = location
//        guesses[currentRound]?.guessTime = timeTaken
//        guesses[currentRound]?.hint = hint

//         // Increment round and reset streetview counter.
//        currentRound += 1
//        remainingStreetViewTimer = 0

//        val numRounds = gameConfig?.getInt("num_rounds") ?: throw IllegalStateException("No game config.")

//        if (currentRound < viewModel.getNumRounds()) {
//            navigateTo(R.id.nav_to_streetview)
//        } else {
//
//            navigateTo(R.id.nav_to_game_over)
//        }
            }
        }
    }

}