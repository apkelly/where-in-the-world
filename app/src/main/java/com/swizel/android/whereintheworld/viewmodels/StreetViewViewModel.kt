package com.swizel.android.whereintheworld.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.navigation.GuessLocationNavKey
import com.swizel.android.whereintheworld.screens.StreetViewUiState
import com.swizel.android.whereintheworld.usecases.GetCurrentRoundUseCase
import com.swizel.android.whereintheworld.usecases.RecordStreetViewTimeUseCase
import com.swizel.android.whereintheworld.usecases.RequestHintUseCase
import kotlin.time.Duration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class StreetViewViewModel(
    private val getCurrentRoundUseCase: GetCurrentRoundUseCase,
    private val recordStreetViewTimeUseCase: RecordStreetViewTimeUseCase,
    private val requestHintUseCase: RequestHintUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<StreetViewUiState>>(UiState(isLoading = LoadingType.LOADING))
    val uiState = _uiState.asStateFlow()

    fun fetchUiState() {
        viewModelScope.launch {
            val currentRound = getCurrentRoundUseCase(Unit)
            _uiState.value = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = StreetViewUiState(
                    numRounds = currentRound.numRounds,
                    currentRound = currentRound.currentRound,
                    timeAllowed = currentRound.timeAllowed,
                    panoramaLatLng = currentRound.panoramaLatLng,
                    landmark = currentRound.landmark,
                    country = currentRound.country,
                    gameDifficulty = currentRound.gameDifficulty,
                    currentHint = currentRound.currentHint,
                    revealedHints = currentRound.revealedHints,
                ),
            )
        }
    }

    sealed class Action {
        data class GuessLocation(val timeTaken: Duration) : Action()
        data class HintRequested(val hint: Hint) : Action()
    }

    fun onAction(
        action: Action,
        navigateTo: (NavKey) -> Unit,
    ) {
        when (action) {
            is Action.GuessLocation -> {
                _uiState.value = UiState(
                    isLoading = LoadingType.LOADING,
                )
                viewModelScope.launch {
                    recordStreetViewTimeUseCase(RecordStreetViewTimeUseCase.Params(timeTaken = action.timeTaken))
                    navigateTo(GuessLocationNavKey)
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
