package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.navigation.GameOverNavKey
import com.swizel.android.whereintheworld.navigation.StreetViewNavKey
import com.swizel.android.whereintheworld.screens.GuessLocationUiState
import com.swizel.android.whereintheworld.usecases.GetCurrentRoundProgressUseCase
import com.swizel.android.whereintheworld.usecases.RequestHintUseCase
import com.swizel.android.whereintheworld.usecases.SubmitGuessUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class GuessLocationViewModel(
    private val getCurrentRoundProgressUseCase: GetCurrentRoundProgressUseCase,
    private val requestHintUseCase: RequestHintUseCase,
    private val submitGuessUseCase: SubmitGuessUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<GuessLocationUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState() {
        viewModelScope.launch {
            val guessLocationState = getCurrentRoundProgressUseCase(Unit)
            _uiState.value = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = GuessLocationUiState(
                    numRounds = guessLocationState.numRounds,
                    currentRound = guessLocationState.currentRound,
                    landmark = guessLocationState.landmark,
                    country = guessLocationState.country,
                    gameDifficulty = guessLocationState.gameDifficulty,
                    currentHint = guessLocationState.currentHint,
                    revealedHints = guessLocationState.revealedHints,
                ),
            )
        }
    }

    sealed class Action {
        data class GuessLocation(
            val location: LatLng?,
        ) : Action()

        data class HintRequested(
            val hint: Hint,
        ) : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
    ) {
        when (action) {
            is Action.GuessLocation -> {
                viewModelScope.launch {
                    when (submitGuessUseCase(SubmitGuessUseCase.Params(location = action.location))) {
                        SubmitGuessUseCase.Result.NextRound -> navigateTo(StreetViewNavKey)
                        SubmitGuessUseCase.Result.GameComplete -> navigateTo(GameOverNavKey)
                    }
                }
            }

            is Action.HintRequested -> {
                viewModelScope.launch {
                    val scoringHint = requestHintUseCase(RequestHintUseCase.Params(hint = action.hint))
                    _uiState.value = _uiState.value.copy(
                        data = _uiState.value.data?.let { data ->
                            data.copy(
                                currentHint = scoringHint,
                                revealedHints = if (action.hint in data.revealedHints) {
                                    data.revealedHints
                                } else {
                                    data.revealedHints + action.hint
                                },
                            )
                        },
                    )
                }
            }
        }
    }
}
